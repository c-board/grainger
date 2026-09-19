'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { fetchJson } from '@/lib/api';
import { formatMoney } from '@/lib/format';
import { Product } from '@/lib/types';

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
      {error ? <p className="mb-4 text-grainger">{error}</p> : null}
      <div className="overflow-x-auto rounded-lg border border-navy/10 bg-white shadow-sm">
        <table className="min-w-full text-left text-sm">
          <thead className="bg-navy/5 text-xs uppercase tracking-wide text-steel">
            <tr>
              <th className="px-5 py-3">SKU</th>
              <th className="px-5 py-3">Product</th>
              <th className="px-5 py-3">Category</th>
              <th className="px-5 py-3 text-right">Cost</th>
              <th className="px-5 py-3 text-right">List</th>
              <th className="px-5 py-3 text-right">Competitor</th>
              <th className="px-5 py-3 text-right">Recommended</th>
              <th className="px-5 py-3">Status</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.sku} className="border-t border-navy/5">
                <td className="px-5 py-3 font-mono text-xs">{product.sku}</td>
                <td className="px-5 py-3">
                  <div>{product.name}</div>
                  <div className="text-xs text-steel">{product.unit}</div>
                </td>
                <td className="px-5 py-3">{product.category}</td>
                <td className="px-5 py-3 text-right">{formatMoney(product.cost)}</td>
                <td className="px-5 py-3 text-right">{formatMoney(product.listPrice)}</td>
                <td className="px-5 py-3 text-right">
                  <div>{formatMoney(product.competitorPrice)}</div>
                  <div className="text-xs text-steel">{product.competitor ?? ''}</div>
                </td>
                <td className="px-5 py-3 text-right font-medium">{formatMoney(product.recommendedPrice)}</td>
                <td className="px-5 py-3 text-xs">{product.recommendationStatus ?? '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </AppShell>
  );
};

export default ProductsPage;
