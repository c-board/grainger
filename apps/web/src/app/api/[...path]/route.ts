const backend = process.env.API_URL ?? 'http://localhost:8080';

type RouteContext = {
  params: Promise<{ path: string[] }>;
};

async function proxy(request: Request, path: string[]): Promise<Response> {
  const incoming = new URL(request.url);
  const target = `${backend}/api/${path.join('/')}${incoming.search}`;
  const init: RequestInit = {
    method: request.method,
    headers: { 'Content-Type': 'application/json' },
  };
  if (request.method !== 'GET' && request.method !== 'HEAD') {
    init.body = await request.text();
  }
  const upstream = await fetch(target, init);
  return new Response(await upstream.text(), {
    status: upstream.status,
    headers: {
      'Content-Type': upstream.headers.get('Content-Type') ?? 'application/json',
    },
  });
}

export const dynamic = 'force-dynamic';

export async function GET(request: Request, context: RouteContext): Promise<Response> {
  const { path } = await context.params;
  return proxy(request, path);
}

export async function POST(request: Request, context: RouteContext): Promise<Response> {
  const { path } = await context.params;
  return proxy(request, path);
}
