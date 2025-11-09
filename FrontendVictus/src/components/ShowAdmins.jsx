import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Dashboard from "./Dashboard"; // Asegúrate que la ruta sea correcta

function ShowAdmins() {
    const navigate = useNavigate();

    useEffect(() => {
        // Verificar si hay un administrador autenticado
        const currentAdmin = sessionStorage.getItem("currentAdmin");
        
        if (!currentAdmin) {
            // Si no hay admin autenticado, redirigir al login
            navigate("/LoginAdminPage");
        }
    }, [navigate]);

    return (
        <div>
            <Dashboard />
        </div>
    );
}

export default ShowAdmins;