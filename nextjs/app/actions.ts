'use server';

import { cookies } from 'next/headers';

export async function setVisitorId(visitorId: string) {
    const cookieStore = await cookies();
    cookieStore.set('visitor_id', visitorId);
}