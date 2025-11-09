import { createContext, useEffect, useState } from "react";

const mockPropertys = [
    {
        id: "7803ce6c-6ade-5db1-a194-5eaff3fb58ca",
        propertyType: "Apartamento",
        propertyNumber: "102",
    },
    {
        id: "7803ce6c-7ade-5db1-a194-5eaff3fb58ca",
        propertyType: "Casa",
        propertyNumber: "10",
    },
    {
        id: "7803ce6c-8ade-5db1-a194-5eaff3fb58ca",
        propertyType: "Apartamento",
        propertyNumber: "304",
    },
    {
        id: "7803ce6c-9ade-5db1-a194-5eaff3fb58ca",
        propertyType: "Casa",
        propertyNumber: "11",
    }
];

const PropertyContext = createContext();

function PropertyProviderWrapper(props) {
    const [propertys, setPropertys] = useState([]);
    const [filteredPropertys, setFilteredPropertys] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);


    // Obtener todos los administradores
    const getProperty = async () => {
        try {
        // Simulamos un pequeño retraso para imitar una petición a API
            setTimeout(() => {
            setPropertys(mockPropertys);
            setFilteredPropertys(mockPropertys);
            setError(null);
            setLoading(false);
            }, 500);
        } catch (err) {
        console.error("Error al cargar administradores:", err);
        setError("No se pudo cargar la lista de administradores. Intenta más tarde.");
        setLoading(false);
        }
    };

    useEffect(() => {
        getProperty();
    }, []);

    // Buscar por nombre
    const filterPropertys = (name) => {
        const filtered = propertys.filter(property =>
            property.name.toLowerCase().includes(name.toLowerCase())
        );
        setFilteredPropertys(filtered);
    };

    const findPropertysByName = async (name) => {
        return new Promise((resolve) => {
            setTimeout(() => {
                const filtered = mockPropertys.filter((property) =>
                property.name.toLowerCase().includes(name.toLowerCase())
                );
                resolve(filtered);
            }, 300);
        });
    };


    // Crear nueva Vivienda
    const createProperty = (newProperty) => {
        return new Promise((resolve) => {
            setTimeout(() => {
                // Generamos un ID único
                const id = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
                const propertyWithId = { ...newProperty, id };
                
                // Actualizamos el estado con la nueva Vivienda
                setPropertys(prevPropertys => [...prevPropertys, propertyWithId]);
                setFilteredPropertys(prevFiltered => [...prevFiltered, propertyWithId]);
                
                // También actualizamos nuestros datos mock para mantener consistencia
                mockPropertys.push(propertyWithId);
                
                resolve({ success: true, data: propertyWithId });
            }, 300);
        });
    };

    // Eliminar un administrador por ID
    const deleteProperty = async (id) => {
        return new Promise((resolve) => {
            setTimeout(() => {
                // Filtramos el administrador del estado
                const updatedPropertys = propertys.filter(property => property.id !== id);
                setPropertys(updatedPropertys);
                setFilteredPropertys(updatedPropertys);
                
                // También actualizamos nuestros datos mock
                const indexToRemove = mockPropertys.findIndex(property => property.id === id);
                if (indexToRemove !== -1) {
                mockPropertys.splice(indexToRemove, 1);
                }
                
                resolve({ success: true });
            }, 300);
        });
    };

    // Modificar un administrador por ID
    const updateProperty = async (id, updatedData) => {
        return new Promise((resolve) => {
            setTimeout(() => {
                // Actualizamos en el estado
                const updatedPropertys = propertys.map(property => 
                property.id === id ? { ...property, ...updatedData } : property
                );
                setPropertys(updatedPropertys);
                setFilteredPropertys(updatedPropertys);
                
                // También actualizamos nuestros datos mock
                const propertyIndex = mockPropertys.findIndex(property => property.id === id);
                if (propertyIndex !== -1) {
                mockPropertys[propertyIndex] = { ...mockPropertys[propertyIndex], ...updatedData };
                }
                
                resolve({ success: true });
            }, 300);
        });
    };

    return (
        <PropertyContext.Provider value={{
            propertys,
            filteredPropertys,
            setPropertys,
            getProperty,
            filterPropertys,
            createProperty,
            deleteProperty,
            updateProperty,
            findPropertysByName,
            error,
            loading,
        }}>
            {props.children}
        </PropertyContext.Provider>
    );
}

export { PropertyContext, PropertyProviderWrapper };
