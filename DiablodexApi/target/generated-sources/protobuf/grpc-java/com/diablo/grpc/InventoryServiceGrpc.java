package com.diablo.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: diablo_inventory.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class InventoryServiceGrpc {

  private InventoryServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "diablopb.InventoryService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.diablo.grpc.ItemRequest,
      com.diablo.grpc.ItemResponse> getGetItemMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetItem",
      requestType = com.diablo.grpc.ItemRequest.class,
      responseType = com.diablo.grpc.ItemResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.diablo.grpc.ItemRequest,
      com.diablo.grpc.ItemResponse> getGetItemMethod() {
    io.grpc.MethodDescriptor<com.diablo.grpc.ItemRequest, com.diablo.grpc.ItemResponse> getGetItemMethod;
    if ((getGetItemMethod = InventoryServiceGrpc.getGetItemMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getGetItemMethod = InventoryServiceGrpc.getGetItemMethod) == null) {
          InventoryServiceGrpc.getGetItemMethod = getGetItemMethod =
              io.grpc.MethodDescriptor.<com.diablo.grpc.ItemRequest, com.diablo.grpc.ItemResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetItem"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.ItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.ItemResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("GetItem"))
              .build();
        }
      }
    }
    return getGetItemMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.diablo.grpc.ItemTypeRequest,
      com.diablo.grpc.ItemResponse> getListItemsByTypeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListItemsByType",
      requestType = com.diablo.grpc.ItemTypeRequest.class,
      responseType = com.diablo.grpc.ItemResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.diablo.grpc.ItemTypeRequest,
      com.diablo.grpc.ItemResponse> getListItemsByTypeMethod() {
    io.grpc.MethodDescriptor<com.diablo.grpc.ItemTypeRequest, com.diablo.grpc.ItemResponse> getListItemsByTypeMethod;
    if ((getListItemsByTypeMethod = InventoryServiceGrpc.getListItemsByTypeMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getListItemsByTypeMethod = InventoryServiceGrpc.getListItemsByTypeMethod) == null) {
          InventoryServiceGrpc.getListItemsByTypeMethod = getListItemsByTypeMethod =
              io.grpc.MethodDescriptor.<com.diablo.grpc.ItemTypeRequest, com.diablo.grpc.ItemResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListItemsByType"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.ItemTypeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.ItemResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("ListItemsByType"))
              .build();
        }
      }
    }
    return getListItemsByTypeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.diablo.grpc.CreateItemRequest,
      com.diablo.grpc.BulkCreateResponse> getCreateBulkLootMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateBulkLoot",
      requestType = com.diablo.grpc.CreateItemRequest.class,
      responseType = com.diablo.grpc.BulkCreateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.diablo.grpc.CreateItemRequest,
      com.diablo.grpc.BulkCreateResponse> getCreateBulkLootMethod() {
    io.grpc.MethodDescriptor<com.diablo.grpc.CreateItemRequest, com.diablo.grpc.BulkCreateResponse> getCreateBulkLootMethod;
    if ((getCreateBulkLootMethod = InventoryServiceGrpc.getCreateBulkLootMethod) == null) {
      synchronized (InventoryServiceGrpc.class) {
        if ((getCreateBulkLootMethod = InventoryServiceGrpc.getCreateBulkLootMethod) == null) {
          InventoryServiceGrpc.getCreateBulkLootMethod = getCreateBulkLootMethod =
              io.grpc.MethodDescriptor.<com.diablo.grpc.CreateItemRequest, com.diablo.grpc.BulkCreateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateBulkLoot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.CreateItemRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.diablo.grpc.BulkCreateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InventoryServiceMethodDescriptorSupplier("CreateBulkLoot"))
              .build();
        }
      }
    }
    return getCreateBulkLootMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static InventoryServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceStub>() {
        @java.lang.Override
        public InventoryServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceStub(channel, callOptions);
        }
      };
    return InventoryServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static InventoryServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceBlockingStub>() {
        @java.lang.Override
        public InventoryServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceBlockingStub(channel, callOptions);
        }
      };
    return InventoryServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static InventoryServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InventoryServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InventoryServiceFutureStub>() {
        @java.lang.Override
        public InventoryServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InventoryServiceFutureStub(channel, callOptions);
        }
      };
    return InventoryServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 1. UNARY
     * </pre>
     */
    default void getItem(com.diablo.grpc.ItemRequest request,
        io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetItemMethod(), responseObserver);
    }

    /**
     * <pre>
     * 2. SERVER STREAMING
     * </pre>
     */
    default void listItemsByType(com.diablo.grpc.ItemTypeRequest request,
        io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListItemsByTypeMethod(), responseObserver);
    }

    /**
     * <pre>
     * 3. CLIENT STREAMING
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.diablo.grpc.CreateItemRequest> createBulkLoot(
        io.grpc.stub.StreamObserver<com.diablo.grpc.BulkCreateResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getCreateBulkLootMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service InventoryService.
   */
  public static abstract class InventoryServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return InventoryServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service InventoryService.
   */
  public static final class InventoryServiceStub
      extends io.grpc.stub.AbstractAsyncStub<InventoryServiceStub> {
    private InventoryServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. UNARY
     * </pre>
     */
    public void getItem(com.diablo.grpc.ItemRequest request,
        io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetItemMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 2. SERVER STREAMING
     * </pre>
     */
    public void listItemsByType(com.diablo.grpc.ItemTypeRequest request,
        io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getListItemsByTypeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 3. CLIENT STREAMING
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.diablo.grpc.CreateItemRequest> createBulkLoot(
        io.grpc.stub.StreamObserver<com.diablo.grpc.BulkCreateResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getCreateBulkLootMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service InventoryService.
   */
  public static final class InventoryServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<InventoryServiceBlockingStub> {
    private InventoryServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. UNARY
     * </pre>
     */
    public com.diablo.grpc.ItemResponse getItem(com.diablo.grpc.ItemRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetItemMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 2. SERVER STREAMING
     * </pre>
     */
    public java.util.Iterator<com.diablo.grpc.ItemResponse> listItemsByType(
        com.diablo.grpc.ItemTypeRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getListItemsByTypeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service InventoryService.
   */
  public static final class InventoryServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<InventoryServiceFutureStub> {
    private InventoryServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InventoryServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InventoryServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 1. UNARY
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.diablo.grpc.ItemResponse> getItem(
        com.diablo.grpc.ItemRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetItemMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ITEM = 0;
  private static final int METHODID_LIST_ITEMS_BY_TYPE = 1;
  private static final int METHODID_CREATE_BULK_LOOT = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ITEM:
          serviceImpl.getItem((com.diablo.grpc.ItemRequest) request,
              (io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse>) responseObserver);
          break;
        case METHODID_LIST_ITEMS_BY_TYPE:
          serviceImpl.listItemsByType((com.diablo.grpc.ItemTypeRequest) request,
              (io.grpc.stub.StreamObserver<com.diablo.grpc.ItemResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_BULK_LOOT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.createBulkLoot(
              (io.grpc.stub.StreamObserver<com.diablo.grpc.BulkCreateResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetItemMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.diablo.grpc.ItemRequest,
              com.diablo.grpc.ItemResponse>(
                service, METHODID_GET_ITEM)))
        .addMethod(
          getListItemsByTypeMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.diablo.grpc.ItemTypeRequest,
              com.diablo.grpc.ItemResponse>(
                service, METHODID_LIST_ITEMS_BY_TYPE)))
        .addMethod(
          getCreateBulkLootMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.diablo.grpc.CreateItemRequest,
              com.diablo.grpc.BulkCreateResponse>(
                service, METHODID_CREATE_BULK_LOOT)))
        .build();
  }

  private static abstract class InventoryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    InventoryServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.diablo.grpc.InventoryProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("InventoryService");
    }
  }

  private static final class InventoryServiceFileDescriptorSupplier
      extends InventoryServiceBaseDescriptorSupplier {
    InventoryServiceFileDescriptorSupplier() {}
  }

  private static final class InventoryServiceMethodDescriptorSupplier
      extends InventoryServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    InventoryServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (InventoryServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new InventoryServiceFileDescriptorSupplier())
              .addMethod(getGetItemMethod())
              .addMethod(getListItemsByTypeMethod())
              .addMethod(getCreateBulkLootMethod())
              .build();
        }
      }
    }
    return result;
  }
}
