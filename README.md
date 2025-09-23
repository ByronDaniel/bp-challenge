# bp-challenge

cd backend/msa-client
docker build -t msa-client .
docker run -p 8080:8080 --name msa-client --network app-network msa-client

cd backend/msa-account
docker build -t msa-account .
docker run -p 8081:8080 --name msa-account --network app-network msa-account

cd backend/msa-movement
docker build -t msa-movement .
docker run -p 8082:8080 --name msa-movement --network app-network msa-movement

cd backend/msa-report
docker build -t msa-report .
docker run -p 8083:8080 --name msa-report --network app-network msa-report