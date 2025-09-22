# bp-challenge

cd backend/msa-client
docker build -t msa-client .
docker run -p 8080:8080 --name msa-client --network app-network msa-client

cd backend/msa-account
docker build -t msa-account .
docker run -p 8081:8081 --name msa-account --network app-network msa-account

cd backend/msa-movement
docker build -t msa-movement .
docker run -p 8082:8082 --name msa-movement --network app-network msa-movement
