import { useState, useCallback } from "react";
import { useNavigate, Link } from "react-router-dom";
import Header from "./Header";
import "../cssComponents/LoginAdmin.css";

const LoginAdmin = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!username || !password) {
            setError("Por favor, complete todos los campos");
            return;
        }
        try {
            const params = new URLSearchParams();
            params.append('grant_type', 'password');
            params.append('username', username);
            params.append('password', password);
            params.append('client_id', 'client-app');
            // Si tu servidor requiere client_secret, agrega:
            // params.append('client_secret', 'TU_CLIENT_SECRET');

            const response = await fetch('http://127.0.0.1:9000/oauth2/token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json',
                },
                body: params
            });

            if (!response.ok) {
                throw new Error('Credenciales inválidas o error de autenticación');
            }

            const data = await response.json();
            if (data.access_token) {
                sessionStorage.setItem('access_token', data.access_token);
                navigate("/ShowAdmins");
            } else {
                throw new Error('No se recibió el token de acceso');
            }
        } catch (error) {
            console.error('Error:', error);
            setError("Error en la autenticación. Por favor, verifique sus credenciales.");
        }
    };

    // Función para obtener el token usando el código de autorización
    const getTokenWithCode = useCallback(async (code) => {
        try {
            const formData = new FormData();
            formData.append('code', code);
            formData.append('grant_type', 'authorization_code');
            formData.append('redirect_uri', 'http://127.0.0.1:8080/authorized');
            formData.append('client_id', 'client-app');

            const response = await fetch('http://127.0.0.1:9000/oauth2/token', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error('Error al obtener el token');
            }

            const data = await response.json();
            
            if (data.access_token) {
                // Guardar el token en sessionStorage
                sessionStorage.setItem('access_token', data.access_token);
                // Redirigir a la página de administradores
                navigate("/ShowAdmins");
            } else {
                throw new Error('No se recibió el token de acceso');
            }
        } catch (error) {
            console.error('Error:', error);
            setError("Error en la autenticación. Por favor, intente nuevamente.");
        }
    }, [navigate, setError]);

    return (
        <>
            <Header />
            <div className="containerFather">
                <Link className="ButtonAccept" to={"/"}>
                    Home
                </Link>
                <div className="container">
                    <h2 className="text-initial">Iniciar Sesión Como Admin</h2>
                    {error && <div className="error-message">{error}</div>}
                    <form onSubmit={handleSubmit}>
                        <div className="information">
                            <label className="textInput" htmlFor="username">
                                Usuario
                            </label>
                            <input
                                id="username"
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Ingrese su usuario"
                            />
                        </div>
                        <div className="information">
                            <label className="textInput" htmlFor="password">
                                Contraseña
                            </label>
                            <input
                                id="password"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Ingrese su contraseña"
                            />
                        </div>
                        <div className="linksWithButton">
                            <button className="ButtonAccept" type="submit">
                                Iniciar Sesión
                            </button>
                            <a className="linkForgetPassword" href="#">
                                ¿Olvidó la contraseña?
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default LoginAdmin;
