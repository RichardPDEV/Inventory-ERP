CREATE TABLE IF NOT EXISTS products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  sku VARCHAR(80) NOT NULL UNIQUE,
  price DECIMAL(19, 4) NOT NULL,
  stock INTEGER NOT NULL DEFAULT 0,
  category_id BIGINT NOT NULL REFERENCES categories(id),
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id)
);

CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_supplier_id ON products(supplier_id);
