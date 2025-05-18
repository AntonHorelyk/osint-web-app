import { render, screen } from '@testing-library/react'
import App from './App'

test('renders scan button', () => {
  render(<App />)
  expect(screen.getByRole('button', { name: /start scan/i })).toBeInTheDocument()
})