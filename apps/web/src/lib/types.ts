export type CustomerSegment = 'LIST' | 'CONTRACT' | 'STRATEGIC';

export type RecommendationStatus =
  | 'PENDING_APPROVAL'
  | 'AUTO_APPLIED'
  | 'APPROVED'
  | 'REJECTED'
  | 'SUPERSEDED';

export type Product = {
  sku: string;
  name: string;
  category: string;
  unit: string;
  cost: number;
  listPrice: number;
  competitorPrice: number | null;
  competitor: string | null;
  recommendedPrice: number;
  recommendationStatus: RecommendationStatus | null;
};

export type Customer = {
  id: string;
  name: string;
  segment: CustomerSegment;
  discount: number;
};

export type DerivationStep = {
  name: string;
  value: number | null;
  note: string;
};

export type PriceBreakdown = {
  sku: string;
  productName: string;
  customerId: string;
  customerName: string;
  segment: CustomerSegment;
  cost: number;
  minMargin: number;
  floor: number;
  listPrice: number;
  segmentDiscount: number;
  segmentPrice: number;
  competitor: string | null;
  competitorPrice: number | null;
  undercutThreshold: number;
  currentPrice: number;
  currentSource: string;
  recommendedPrice: number;
  matchedCompetitor: boolean;
  rationale: string;
  steps: DerivationStep[];
};

export type Recommendation = {
  id: string;
  sku: string;
  productName: string;
  customerId: string;
  customerName: string;
  currentPrice: number;
  recommendedPrice: number;
  floorPrice: number;
  competitorPrice: number | null;
  deltaPct: number;
  status: RecommendationStatus;
  rationale: string;
  createdAt: string;
};

export type CompetitorMove = {
  sku: string;
  productName: string;
  competitor: string;
  amount: number;
  observedAt: string;
};

export type Dashboard = {
  pendingApprovals: number;
  autoApplied: number;
  approved: number;
  rejected: number;
  recentCompetitorMoves: CompetitorMove[];
};
