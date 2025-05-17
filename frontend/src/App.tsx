import { useState } from 'react'
import type { FormEvent } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import type { ScanDto } from './api'
import { fetchScans, createScan } from './api'
import './App.css'

export function App() {
  const qc = useQueryClient()

  const { data: scans = [], isLoading, error } = useQuery<ScanDto[], Error>({
    queryKey: ['scans'],
    queryFn: fetchScans,
  })

  const mutation = useMutation<ScanDto, Error, string>({
    mutationFn: createScan,
    onSuccess: () => qc.invalidateQueries({ queryKey: ['scans'] }),
  })
  const isRunning = mutation.status === 'pending'

  const [domainInput, setDomainInput] = useState('')
  const [selected, setSelected] = useState<ScanDto | null>(null)

  if (isLoading) return <div className="text-center py-10">Loading scan history…</div>
  if (error)     return <div className="text-center text-red-500 py-10">Error: {error.message}</div>

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault()
    const domain = domainInput.trim()
    if (!domain) return
    mutation.mutate(domain)
    setDomainInput('')
  }

  return (
    <div className="app-container">
      <h1>
        OSINT Scanner
      </h1>

      <form onSubmit={handleSubmit} className="search-bar">
        <input
          type="text"
          value={domainInput}
          onChange={e => setDomainInput(e.target.value)}
          placeholder="Enter domain, e.g. example.com"
          />
        <button
          type="submit"
          disabled={isRunning}
          className={`
            btn-primary
            ${isRunning ? 'cursor-not-allowed' : 'hover:bg-[var(--mint)]'}
          `}
        >
          {isRunning ? 'Scanning…' : 'Start Scan'}
        </button>
      </form>

      <div className="cards-grid px-4">
        {scans.map(scan => (
          <div key={scan.id} className="card">
            <h2 >{scan.domain}</h2>
            <p className="text-sm mb-1">
              Started: {new Date(scan.start).toLocaleString()}
            </p>
            <p className="text-sm mb-3">
              Finished: {scan.end ? new Date(scan.end).toLocaleString() : '–'}
            </p>
            <p className="count">Artifacts found: {scan.results.length}</p>
            <button
              onClick={() => setSelected(scan)}
              className="mt-4 btn-secondary hover:bg-[var(--mint)]"
            >
              Details
            </button>
          </div>
        ))}
      </div>

      {selected && (
        <div className="modal-backdrop" onClick={() => setSelected(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h3 >
              {selected.domain} — Details
            </h3>
            <ul>
              {selected.results.map((r, i) => (
                <li key={i} className="text-sm break-all">{r}</li>
              ))}
            </ul>
            <div>
              <button
                onClick={() => setSelected(null)}
                className="btn-secondary"
              >
                Close
              </button>
              <a
                href={`/api/scans/${selected.id}`}
                target="_blank"
                rel="noopener noreferrer"
                className="btn-primary"
              >
                Export to XLSX
              </a>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}