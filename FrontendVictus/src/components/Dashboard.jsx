import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../cssComponents/AdminDashboard.css";

const buttonData = [
    { label: "Conjuntos", icon: "🏢", path: "/" },
    { label: "Zonas Comunes", icon: "🧱", path: "/CommonZone" },
    { label: "Agendas", icon: "📆", path: "/" },
    { label: "Administradores", icon: "👥", path: "/ManagementAdmin" },
    { label: "Porteros", icon: "🧍", path: "/" },
    { label: "Viviendas", icon: "🏠", path: "/Property" },
    { label: "Residentes", icon: "👨‍👩‍👧", path: "/" },
    { label: "Asignar Usuarios", icon: "✅", path: "/" },
];

function AdminDashboard() {
    const navigate = useNavigate();
    const [adminName, setAdminName] = useState("");
    
    useEffect(() => {
        // Verificar si hay un administrador autenticado
        const currentAdmin = sessionStorage.getItem("currentAdmin");
        
        if (currentAdmin) {
            const admin = JSON.parse(currentAdmin);
            setAdminName(`${admin.name} ${admin.lastName}`);
        } else {
            // Si no hay admin autenticado, redirigir al login
            navigate("/loginAdmin");
        }
    }, [navigate]);

    const handleLogout = () => {
        // Eliminar los datos del administrador y redirigir al login
        sessionStorage.removeItem("currentAdmin");
        navigate("/loginAdmin");
    };

    return (
        <>
            <div className="admin-dashboard">
                <header className="admin-topbar">
                    <h2 className="adminNameTittle">Hola, <span>{adminName || "Administrador"}</span></h2>
                    <button className="ButtonAccept" onClick={handleLogout}>
                        Cerrar Sesión
                    </button>
                </header>

                <div className="admin-grid">
                    {buttonData.map((btn, index) => (
                        <div
                            key={index}
                            className="admin-card"
                            onClick={() => navigate(btn.path)}
                        >
                            <div className="admin-icon">{btn.icon}</div>
                            <p>{btn.label}</p>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default AdminDashboard;