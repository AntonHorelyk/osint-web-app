import { useState } from "react";
import {
  QueryClient,
  QueryClientProvider,
  useMutation,
} from "@tanstack/react-query";
import { createScan } from "./api";
import type { ScanResponse } from "./api";

const qc = new QueryClient();

export default function App() {
  return (
    <QueryClientProvider client={qc}>
      <ScanForm />
    </QueryClientProvider>
  );
}

function ScanForm() {
  const [domain, setDomain] = useState("");
  const mutation = useMutation<ScanResponse, Error, string>({
    mutationFn: createScan,
    onSuccess: (data) =>
      alert(`Скан ${data.id} для ${data.domain} поставлен в очередь`),
    onError: (err) => alert(err.message),
  });

  return (
    <div className="flex flex-col gap-4 p-8 max-w-sm mx-auto">
      <h1 className="text-2xl text-center mb-4">OSINT Scanner</h1>

      <input
        className="border rounded p-2"
        placeholder="example.com"
        value={domain}
        onChange={(e) => setDomain(e.target.value)}
      />

      <button
        className="bg-blue-600 hover:bg-blue-700 text-white rounded p-2 disabled:opacity-50"
        onClick={() => mutation.mutate(domain)}
        disabled={mutation.isPending || !domain.trim()}
      >
        {mutation.isPending ? "Запускаю…" : "Старт"}
      </button>
    </div>
  );
}