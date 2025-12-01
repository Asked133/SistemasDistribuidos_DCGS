package com.example.DiablodexApi.service;

import com.diablo.grpc.*;
import com.example.DiablodexApi.dto.BulkCreateResponseDto;
import com.example.DiablodexApi.dto.CreateItemDto;
import com.example.DiablodexApi.dto.ItemDto;
import com.example.DiablodexApi.exception.GrpcServiceException;
import com.example.DiablodexApi.exception.InvalidArgumentException;
import com.example.DiablodexApi.exception.ItemAlreadyExistsException;
import com.example.DiablodexApi.exception.ItemNotFoundException;
import com.example.DiablodexApi.exception.OutOfRangeException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    @GrpcClient("weaponsApi")
    private InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub;

    @GrpcClient("weaponsApi")
    private InventoryServiceGrpc.InventoryServiceStub asyncStub;

    @Cacheable(value = "items", key = "#id")
    public ItemDto getItemById(String id) {
        try {
            log.info("Fetching item with id: {}", id);
            ItemRequest request = ItemRequest.newBuilder()
                    .setId(id)
                    .build();
            
            ItemResponse response = blockingStub.getItem(request);
            log.info("Successfully fetched item: {}", response.getNombre());
            return mapToDto(response);
        } catch (StatusRuntimeException e) {
            handleGrpcException(e, "Item with id " + id + " not found");
            throw new GrpcServiceException("Error calling gRPC service: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "itemsByType", key = "#type")
    public List<ItemDto> getItemsByType(String type) {
        try {
            log.info("Fetching items by type: {}", type);
            ItemType itemType = parseItemType(type);
            
            ItemTypeRequest request = ItemTypeRequest.newBuilder()
                    .setTipo(itemType)
                    .build();
            
            List<ItemDto> items = new ArrayList<>();
            Iterator<ItemResponse> responseIterator = blockingStub.listItemsByType(request);
            
            while (responseIterator.hasNext()) {
                items.add(mapToDto(responseIterator.next()));
            }
            
            log.info("Successfully fetched {} items of type {}", items.size(), type);
            return items;
        } catch (StatusRuntimeException e) {
            handleGrpcException(e, "Error fetching items by type " + type);
            throw new GrpcServiceException("Error calling gRPC service: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = {"items", "itemsByType"}, allEntries = true)
    public BulkCreateResponseDto createBulkItems(List<CreateItemDto> items) {
        log.info("Creating {} items in bulk", items.size());
        
        final CountDownLatch finishLatch = new CountDownLatch(1);
        final AtomicReference<BulkCreateResponse> responseRef = new AtomicReference<>();
        final AtomicReference<Throwable> errorRef = new AtomicReference<>();

        StreamObserver<BulkCreateResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(BulkCreateResponse response) {
                responseRef.set(response);
            }

            @Override
            public void onError(Throwable t) {
                errorRef.set(t);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };

        StreamObserver<CreateItemRequest> requestObserver = asyncStub.createBulkLoot(responseObserver);

        try {
            for (int index = 0; index < items.size(); index++) {
                CreateItemDto item = items.get(index);
                // Auto-generate ID if not provided
                String itemId = (item.getId() != null && !item.getId().isBlank()) 
                    ? item.getId() 
                    : UUID.randomUUID().toString();
                
                ItemType itemType = validateItemPayload(item, index);
                
                CreateItemRequest.Builder requestBuilder = CreateItemRequest.newBuilder()
                        .setId(itemId)
                        .setNombre(item.getNombre())
                        .setTipo(itemType)
                        .setPoderDeObjeto(item.getPoderDeObjeto())
                        .setHabilidadPasiva(item.getHabilidadPasiva() != null ? item.getHabilidadPasiva() : "")
                        .setHabilidadActiva(item.getHabilidadActiva() != null ? item.getHabilidadActiva() : "");

                if (itemType == ItemType.WEAPON) {
                    requestBuilder.setDanoBase(item.getDanoBase());
                } else if (itemType == ItemType.ARMOR) {
                    requestBuilder.setArmaduraBase(item.getArmaduraBase());
                }

                if (item.getGemas() != null) {
                    for (String gema : item.getGemas()) {
                        requestBuilder.addGemas(parseGemType(gema));
                    }
                }

                requestObserver.onNext(requestBuilder.build());
            }
        } catch (InvalidArgumentException | OutOfRangeException e) {
            requestObserver.onError(e);
            throw e;
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw new GrpcServiceException("Error sending items: " + e.getMessage(), e);
        }

        requestObserver.onCompleted();

        try {
            if (!finishLatch.await(30, TimeUnit.SECONDS)) {
                throw new GrpcServiceException("Timeout waiting for bulk create response");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GrpcServiceException("Interrupted while waiting for bulk create response", e);
        }

        if (errorRef.get() != null) {
            Throwable error = errorRef.get();
            if (error instanceof StatusRuntimeException sre) {
                handleGrpcException(sre, "Error creating bulk items");
            }
            throw new GrpcServiceException("Error creating bulk items: " + error.getMessage(), error);
        }

        BulkCreateResponse response = responseRef.get();
        log.info("Successfully created {} items", response.getItemsCreados());
        
        // Map the created items from the response
        List<ItemDto> createdItems = response.getItemsList().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        
        return BulkCreateResponseDto.builder()
                .itemsCreados(response.getItemsCreados())
                .mensaje(response.getMensaje())
                .items(createdItems)
                .build();
    }

    private ItemDto mapToDto(ItemResponse response) {
        return ItemDto.builder()
                .id(response.getId())
                .nombre(response.getNombre())
                .tipo(itemTypeToString(response.getTipo()))
                .poderDeObjeto(response.getPoderDeObjeto())
                .danoBase(response.hasDanoBase() ? response.getDanoBase() : null)
                .armaduraBase(response.hasArmaduraBase() ? response.getArmaduraBase() : null)
                .habilidadPasiva(response.getHabilidadPasiva())
                .habilidadActiva(response.getHabilidadActiva())
                .gemasIncrustadas(response.getGemasIncrustadasList().stream()
                        .map(this::gemTypeToString)
                        .collect(Collectors.toList()))
                .build();
    }

    private ItemType parseItemType(String type) {
        return parseItemType(type, null);
    }

    private ItemType parseItemType(String type, String context) {
        String prefix = context != null ? context + ": " : "";
        if (type == null || type.isBlank()) {
            throw new InvalidArgumentException(prefix + "Type is required");
        }
        return switch (type.trim().toUpperCase()) {
            case "WEAPON" -> ItemType.WEAPON;
            case "ARMOR" -> ItemType.ARMOR;
            default -> throw new InvalidArgumentException(prefix + "Unsupported item type: " + type);
        };
    }

    private ItemType validateItemPayload(CreateItemDto item, int index) {
        String context = "Item " + (index + 1);

        if (item == null) {
            throw new InvalidArgumentException(context + ": Payload is required");
        }
        if (item.getNombre() == null || item.getNombre().isBlank()) {
            throw new InvalidArgumentException(context + ": Name is required");
        }

        ItemType itemType = parseItemType(item.getTipo(), context);

        Integer power = item.getPoderDeObjeto();
        if (power == null || power < 1 || power > 1000) {
            throw new InvalidArgumentException(context + ": Power must be between 1 and 1000");
        }

        if (item.getGemas() != null && item.getGemas().size() > 2) {
            throw new InvalidArgumentException(context + ": Maximum 2 gems allowed");
        }

        if (itemType == ItemType.WEAPON) {
            if (item.getDanoBase() == null || item.getDanoBase() <= 0) {
                throw new InvalidArgumentException(context + ": Weapons require positive damage");
            }
        } else if (itemType == ItemType.ARMOR) {
            if (item.getArmaduraBase() == null || item.getArmaduraBase() <= 0) {
                throw new InvalidArgumentException(context + ": Armor requires positive defense");
            }
        }

        return itemType;
    }

    private String itemTypeToString(ItemType type) {
        return switch (type) {
            case WEAPON -> "WEAPON";
            case ARMOR -> "ARMOR";
            default -> "UNKNOWN";
        };
    }

    private GemType parseGemType(String gem) {
        if (gem == null) {
            return GemType.UNKNOWN_GEM;
        }
        return switch (gem.toUpperCase()) {
            case "AMATISTA" -> GemType.AMATISTA;
            case "CRANEO" -> GemType.CRANEO;
            case "DIAMANTE" -> GemType.DIAMANTE;
            case "ESMERALDA" -> GemType.ESMERALDA;
            case "RUBI" -> GemType.RUBI;
            case "TOPACIO" -> GemType.TOPACIO;
            case "ZAFIRO" -> GemType.ZAFIRO;
            default -> GemType.UNKNOWN_GEM;
        };
    }

    private String gemTypeToString(GemType gem) {
        return switch (gem) {
            case AMATISTA -> "AMATISTA";
            case CRANEO -> "CRANEO";
            case DIAMANTE -> "DIAMANTE";
            case ESMERALDA -> "ESMERALDA";
            case RUBI -> "RUBI";
            case TOPACIO -> "TOPACIO";
            case ZAFIRO -> "ZAFIRO";
            default -> "UNKNOWN";
        };
    }

    private void handleGrpcException(StatusRuntimeException e, String defaultMessage) {
        Status.Code code = e.getStatus().getCode();
        String description = e.getStatus().getDescription();
        
        switch (code) {
            case NOT_FOUND -> throw new ItemNotFoundException(description != null ? description : defaultMessage);
            case ALREADY_EXISTS -> throw new ItemAlreadyExistsException(description != null ? description : "Item already exists");
            case INVALID_ARGUMENT -> throw new InvalidArgumentException(description != null ? description : defaultMessage);
            case OUT_OF_RANGE -> throw new OutOfRangeException(description != null ? description : defaultMessage);
            default -> log.error("gRPC error: {} - {}", code, description);
        }
    }
}
