export interface ScanDto {
  id: number;
  domain: string;
  start: string;
  end: string;
  results: string[];
}

export async function fetchScans(): Promise<ScanDto[]> {
  const res = await fetch('/api/scans');
  if (!res.ok) throw new Error(`Ошибка при получении списка: ${res.statusText}`);
  return res.json();
}

export async function fetchScan(id: number): Promise<ScanDto> {
  const res = await fetch(`/api/scans/${id}`);
  if (!res.ok) throw new Error(`Ошибка при получении скана #${id}: ${res.statusText}`);
  return res.json();
}

export async function createScan(domain: string): Promise<ScanDto> {
  const res = await fetch('/api/scans', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ domain }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => null);
    throw new Error(err?.error || res.statusText);
  }
  return res.json();
}