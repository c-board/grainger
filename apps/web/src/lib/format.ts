export function formatMoney(value: number | null | undefined): string {
  if (value == null) {
    return '—';
  }
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);
}

export function formatPct(value: number | null | undefined): string {
  if (value == null) {
    return '—';
  }
  return new Intl.NumberFormat('en-US', {
    style: 'percent',
    minimumFractionDigits: 1,
    maximumFractionDigits: 1,
  }).format(value);
}

export function formatTime(value: string): string {
  return new Intl.DateTimeFormat('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    second: '2-digit',
  }).format(new Date(value));
}
