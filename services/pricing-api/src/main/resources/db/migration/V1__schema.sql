CREATE SCHEMA IF NOT EXISTS analytics;

CREATE TABLE products (
  sku VARCHAR(32) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(64) NOT NULL,
  cost NUMERIC(12, 2) NOT NULL,
  unit VARCHAR(32) NOT NULL
);

CREATE TABLE customers (
  id VARCHAR(32) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  segment VARCHAR(32) NOT NULL
);

CREATE TABLE price_rules (
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR(64) NOT NULL UNIQUE,
  min_margin NUMERIC(6, 4) NOT NULL,
  competitor_undercut_threshold NUMERIC(6, 4) NOT NULL
);

CREATE TABLE list_prices (
  sku VARCHAR(32) PRIMARY KEY REFERENCES products (sku),
  amount NUMERIC(12, 2) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE competitor_prices (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(32) NOT NULL REFERENCES products (sku),
  competitor VARCHAR(64) NOT NULL,
  amount NUMERIC(12, 2) NOT NULL,
  observed_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_competitor_prices_sku_observed ON competitor_prices (sku, observed_at DESC);
CREATE INDEX idx_competitor_prices_observed ON competitor_prices (observed_at DESC);

CREATE TABLE applied_prices (
  sku VARCHAR(32) NOT NULL REFERENCES products (sku),
  customer_id VARCHAR(32) NOT NULL REFERENCES customers (id),
  amount NUMERIC(12, 2) NOT NULL,
  source VARCHAR(32) NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  PRIMARY KEY (sku, customer_id)
);

CREATE TABLE price_recommendations (
  id UUID PRIMARY KEY,
  sku VARCHAR(32) NOT NULL REFERENCES products (sku),
  customer_id VARCHAR(32) NOT NULL REFERENCES customers (id),
  current_price NUMERIC(12, 2) NOT NULL,
  recommended_price NUMERIC(12, 2) NOT NULL,
  floor_price NUMERIC(12, 2) NOT NULL,
  competitor_price NUMERIC(12, 2),
  delta_pct NUMERIC(8, 4) NOT NULL,
  status VARCHAR(32) NOT NULL,
  rationale TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  resolved_at TIMESTAMPTZ
);

CREATE INDEX idx_recommendations_status ON price_recommendations (status, created_at DESC);

CREATE TABLE analytics.price_facts (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(32) NOT NULL,
  customer_segment VARCHAR(32),
  list_price NUMERIC(12, 2) NOT NULL,
  recommended_price NUMERIC(12, 2) NOT NULL,
  competitor_price NUMERIC(12, 2),
  floor_price NUMERIC(12, 2) NOT NULL,
  status VARCHAR(32) NOT NULL,
  recorded_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
