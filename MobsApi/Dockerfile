FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
WORKDIR /app


USER app
FROM --platform=$BUILDPLATFORM mcr.microsoft.com/dotnet/sdk:9.0 AS build
ARG configuration=Release
WORKDIR /src
COPY ["MobApi.csproj", "./"]
RUN dotnet restore "MobApi.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "MobApi.csproj" -c $configuration -o /app/build

FROM build AS publish
ARG configuration=Release
RUN dotnet publish "MobApi.csproj" -c ${configuration} -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "MobApi.dll"]