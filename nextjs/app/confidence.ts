import { createConfidenceServerProvider } from '@spotify-confidence/openfeature-server-provider';
import { OpenFeature } from '@openfeature/server-sdk';

const provider = createConfidenceServerProvider({
    clientSecret: process.env.NEXT_PUBLIC_CLIENT_SECRET!,
    fetchImplementation: fetch,
    timeout: 1000,
});

OpenFeature.setProvider(provider);

export const client = OpenFeature.getClient();