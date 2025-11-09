CREATE TABLE IF NOT EXISTS vivienda (
    id UUID PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    conjunto_id UUID NOT NULL,
    metadatos JSONB NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_vivienda_numero_conjunto
    ON vivienda (numero, conjunto_id);
