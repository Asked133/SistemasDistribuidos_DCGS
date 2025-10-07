FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
WORKDIR /app


USER app
FROM --platform=$BUILDPLATFORM mcr.microsoft.com/dotnet/sdk:9.0 AS build
ARG configuration=Release
WORKDIR /src
COPY ["MobedexApi.csproj", "./"]
RUN dotnet restore "MobedexApi.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "MobedexApi.csproj" -c $configuration -o /app/build

FROM build AS publish
ARG configuration=Release
RUN dotnet publish "MobedexApi.csproj" -c ${configuration} -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "MobedexApi.dll"]