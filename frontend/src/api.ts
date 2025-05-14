export interface ScanResponse {
    id: string;
    domain: string;
    startedAt: string;
    status: string;
  }
  
  export async function createScan(domain: string): Promise<ScanResponse> {
    const r = await fetch("/api/scans", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ domain }),
    });
    if (!r.ok) throw new Error("Server error");
    return r.json();
  }