'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Badge } from '@/components/ui/badge';
import { Card, CardContent } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { fetchJson } from '@/lib/api';
import { formatMoney } from '@/lib/format';
import { Product, RecommendationStatus } from '@/lib/types';

function statusLabel(status: RecommendationStatus | null): string {
  if (!status) {
    return '—';
  }
  return status.replaceAll('_', ' ').toLowerCase();
}

const ProductsPage = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      try {
        setProducts(await fetchJson<Product[]>('/api/products'));
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : 'Unable to load products');
      }
    }
    void load();
  }, []);

  return (
    <AppShell
      title="MRO catalog"
      subtitle="List price, latest competitor observation, and the recommended list-segment price."
    >
      {error ? (
        <Alert variant="destructive">
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      ) : null}
      <Card>
        <CardContent className="pt-6">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>SKU</TableHead>
                <TableHead>Product</TableHead>
                <TableHead>Category</TableHead>
                <TableHead className="text-right">Cost</TableHead>
                <TableHead className="text-right">List</TableHead>
                <TableHead className="text-right">Competitor</TableHead>
                <TableHead className="text-right">Recommended</TableHead>
                <TableHead>Status</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {products.map((product) => (
                <TableRow key={product.sku}>
                  <TableCell className="font-mono text-xs">{product.sku}</TableCell>
                  <TableCell>
                    <div>{product.name}</div>
                    <div className="text-xs text-muted-foreground">{product.unit}</div>
                  </TableCell>
                  <TableCell>{product.category}</TableCell>
                  <TableCell className="text-right">{formatMoney(product.cost)}</TableCell>
                  <TableCell className="text-right">{formatMoney(product.listPrice)}</TableCell>
                  <TableCell className="text-right">
                    <div>{formatMoney(product.competitorPrice)}</div>
                    <div className="text-xs text-muted-foreground">{product.competitor ?? ''}</div>
                  </TableCell>
                  <TableCell className="text-right font-medium">{formatMoney(product.recommendedPrice)}</TableCell>
                  <TableCell>
                    <Badge variant="outline" className="capitalize">
                      {statusLabel(product.recommendationStatus)}
                    </Badge>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </AppShell>
  );
};

export default ProductsPage;
