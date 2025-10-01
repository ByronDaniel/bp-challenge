import { Environment } from './environment.type';

export const environment: Environment = {
  production: true,
  apiUrlClient: 'http://msa-client:8080',
  apiUrlAccount: 'http://msa-account:8080',
  apiUrlMovement: 'http://msa-movement:8080',
  apiUrlReport: 'http://msa-report:8080',
};
