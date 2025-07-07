import { NextRequest, NextResponse } from 'next/server'

export function middleware(request: NextRequest) {
  const response = NextResponse.next()
  const pathname = request.nextUrl.pathname
  
  const visitorId = request.cookies.get('visitor_id')
  
  if (!visitorId) {
    const newVisitorId = Math.random().toString(36).substring(2, 15)
    response.cookies.set('visitor_id', newVisitorId)
  }

  if( pathname === "/" ) {
    return NextResponse.redirect(new URL('/na', request.url))
  }
  
  return response
}

export const config = {
  matcher: [
    '/((?!api|_next/static|_next/image|favicon.ico).*)',
  ],
}
