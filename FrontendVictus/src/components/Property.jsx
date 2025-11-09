import React, { useState, useEffect, useContext } from "react";
import "../cssComponents/AdminManagement.css"; // O usa Tailwind si prefieres
import Header from "./Header"
import { PropertyContext } from "../contexts/Property.context";
import { Link } from 'react-router-dom';

function Property() {
  const {
    propertys,
    getProperty,
    createProperty,
    updateProperty,
    deleteProperty,
    findPropertysByName,
    error,
    loading
  } = useContext(PropertyContext);

  const [filteredProperty, setFilteredProperty] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [form, setForm] = useState({
    id: "",
    propertyType: "",
    propertyNumber: "",
  });
  const [isEditing, setIsEditing] = useState(false);
  useEffect(() => {
      getProperty();
    }, []);

  useEffect(() => {
    if(searchTerm.trim()=== ""){
      setFilteredProperty(propertys);
    }else{
        findPropertysByName(searchTerm).then((results) => {
        setFilteredProperty(results);
      });
    }
  }, [searchTerm, propertys]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.propertyType || !form.propertyNumber) return;

    const payload = {
        propertyType: form.propertyType,
        propertyNumber: form.propertyNumber,
    };
    
    if(isEditing && form.id){
      await updateProperty (form.id, payload);
    }else{
      await createProperty(payload);
    }

    await getProperty();

    setForm({id: null, propertyType: "", propertyNumber: ""})
    setIsEditing(false);
  };

  const handleEdit = (property) => {
    setForm({
      id: property.id || "",
      propertyType: property.propertyType || "",
      propertyNumber: property.propertyNumber || "",
    });
    setIsEditing(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Seguro que deseas eliminar esta zona común?")) {
      await deleteProperty(id);
      await getProperty();
    }
  };
  return (
    <>
        <Header />
        <div className="admin-management-container">
          <Link className="ButtonLogOut" to={"/"}>
            Cerrar Sesión
          </Link>
          <Link className="ButtonBack" to={"/ShowAdmins"}>
            Regresar
          </Link>
          <h2>Gestión de Viviendas</h2>
        <form className="admin-form" onSubmit={handleSubmit}>
          <input
            type="text"
            name="propertyType"
            placeholder="Tipo de Vivienda"
            value={form.propertyType}
            onChange={handleInputChange}
            required
          />
          <input
            type="text"
            name="propertyNumber"
            placeholder="Número de la Propiedad"
            value={form.propertyNumber}
            onChange={handleInputChange}
          />
            
            
          <button type="submit">{isEditing ? "Actualizar" : "Crear"}</button>

          {/* 🔍 Buscador */}
          <input
            type="text"
            placeholder="Buscar por Tipo de Vivienda..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </form>
        
          {loading && <p>Cargando Viviendas...</p>}
          {error && <p style={{ color: "red" , margin: 10}}>{error}</p>}

          {/* 📋 Lista */}
          <table className="admin-table">
            <thead>
              <tr>
                <th>Tipo de Vivienda</th>
                <th>Número de Vivienda</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              {filteredProperty?.map((propertys) => (
                <tr key={propertys.id}>
                  <td>{propertys.propertyType}</td>
                  <td>{propertys.propertyNumber}</td>
                  <td>
                    <button onClick={() => handleEdit(propertys)}>Editar</button>
                    <button onClick={() => handleDelete(propertys.id)}>Eliminar</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
            
        </div>
    </>
  )
}

export default Property