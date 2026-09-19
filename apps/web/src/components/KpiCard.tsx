type KpiCardProps = {
  label: string;
  value: string;
  hint: string;
};

export const KpiCard = ({ label, value, hint }: KpiCardProps) => {
  return (
    <div className="rounded-lg border border-navy/10 bg-white p-5 shadow-sm">
      <p className="text-xs uppercase tracking-wide text-steel">{label}</p>
      <p className="mt-2 text-3xl font-semibold text-navy">{value}</p>
      <p className="mt-1 text-sm text-steel">{hint}</p>
    </div>
  );
};
