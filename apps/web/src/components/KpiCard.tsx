import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';

type KpiCardProps = {
  label: string;
  value: string;
  hint: string;
};

export const KpiCard = ({ label, value, hint }: KpiCardProps) => {
  return (
    <Card>
      <CardHeader className="pb-2">
        <CardDescription className="text-xs uppercase tracking-wide">{label}</CardDescription>
        <CardTitle className="text-3xl font-semibold">{value}</CardTitle>
      </CardHeader>
      <CardContent>
        <p className="text-sm text-muted-foreground">{hint}</p>
      </CardContent>
    </Card>
  );
};
