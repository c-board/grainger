INSERT INTO price_rules (category, min_margin, competitor_undercut_threshold) VALUES
  ('FASTENERS', 0.1800, 0.0300),
  ('PPE', 0.2200, 0.0300),
  ('HVAC', 0.2000, 0.0400),
  ('ELECTRICAL', 0.1900, 0.0300);

INSERT INTO products (sku, name, category, cost, unit) VALUES
  ('FAST-M8-25', 'M8-1.25 x 25mm Hex Cap Screw, Zinc', 'FASTENERS', 0.12, 'each'),
  ('FAST-WEDGE-12', '1/2" Wedge Anchor', 'FASTENERS', 0.85, 'each'),
  ('FAST-NUT-M8', 'M8 Nylon Lock Nut', 'FASTENERS', 0.04, 'each'),
  ('FAST-TAP-1024', '10-24 Thread Forming Tap', 'FASTENERS', 3.20, 'each'),
  ('PPE-GLV-CUT5', 'Cut Level 5 HPPE Gloves', 'PPE', 4.20, 'pair'),
  ('PPE-GLS-CLR', 'Clear Safety Glasses, ANSI Z87', 'PPE', 1.10, 'each'),
  ('PPE-EAR-NRR33', 'Disposable Earplugs NRR 33', 'PPE', 0.08, 'pair'),
  ('PPE-VEST-CLS2', 'Class 2 Hi-Vis Vest', 'PPE', 3.50, 'each'),
  ('HVAC-FLT-20X25', '20x25x1 MERV 13 Filter', 'HVAC', 6.40, 'each'),
  ('HVAC-BELT-A36', 'A36 V-Belt', 'HVAC', 5.10, 'each'),
  ('HVAC-THRM-24V', '24V Digital Thermostat', 'HVAC', 18.00, 'each'),
  ('HVAC-CAP-35', '35µF Run Capacitor', 'HVAC', 4.40, 'each'),
  ('ELEC-MCB-20A', '20A Single-Pole Breaker', 'ELECTRICAL', 4.80, 'each'),
  ('ELEC-THHN-12', '12 AWG THHN Wire, 1000 ft', 'ELECTRICAL', 95.00, 'reel'),
  ('ELEC-CONN-12', '1/2" EMT Connector', 'ELECTRICAL', 0.35, 'each'),
  ('ELEC-LED-4FT', '4 ft LED Shop Light', 'ELECTRICAL', 22.00, 'each');

INSERT INTO list_prices (sku, amount, updated_at) VALUES
  ('FAST-M8-25', 0.28, now()),
  ('FAST-WEDGE-12', 1.95, now()),
  ('FAST-NUT-M8', 0.12, now()),
  ('FAST-TAP-1024', 7.60, now()),
  ('PPE-GLV-CUT5', 9.80, now()),
  ('PPE-GLS-CLR', 3.45, now()),
  ('PPE-EAR-NRR33', 0.22, now()),
  ('PPE-VEST-CLS2', 8.90, now()),
  ('HVAC-FLT-20X25', 14.20, now()),
  ('HVAC-BELT-A36', 12.75, now()),
  ('HVAC-THRM-24V', 42.00, now()),
  ('HVAC-CAP-35', 11.25, now()),
  ('ELEC-MCB-20A', 11.40, now()),
  ('ELEC-THHN-12', 189.00, now()),
  ('ELEC-CONN-12', 0.92, now()),
  ('ELEC-LED-4FT', 49.00, now());

INSERT INTO customers (id, name, segment) VALUES
  ('CUST-001', 'Acme Manufacturing', 'CONTRACT'),
  ('CUST-002', 'Midwest Food Plant', 'STRATEGIC'),
  ('CUST-003', 'City of Chicago Facilities', 'CONTRACT'),
  ('CUST-004', 'Harbor Logistics', 'LIST'),
  ('CUST-005', 'Northshore Hospital', 'STRATEGIC'),
  ('CUST-006', 'Lakeside University', 'CONTRACT'),
  ('CUST-007', 'Prairie Construction', 'LIST'),
  ('CUST-008', 'Great Lakes Utilities', 'STRATEGIC');
