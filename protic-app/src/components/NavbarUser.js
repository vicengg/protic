import React, { useState } from 'react';
import { useUser } from '../hooks/useUser';


export const NavbarUser = () => {

    const { data: user } = useUser();
    const [show, setShow] = useState(false);
    
    const toggle = () => {
        setShow(!show);
    }

    const logout = () => {
        fetch("/user/logout").then(response => {
            if(response.status === 200) {
                document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                window.location.replace("https://github.com/logout");
            }
        });
    }

    return (
        <>
            {!!user &&
                <div className={`nav-item dropdown mr-5 navbar-user-clickable ${show && "show"}`} onClick={toggle}>
                    <div className="nav-link dropdown-toggle text-light">
                        <img width="25" height="25" src={user.avatarUrl} className="rounded mr-2" alt={user.login} />
                        <span>{user.login}</span>
                    </div>
                    <div className={`dropdown-menu ${show && "show"}`} aria-labelledby="navbarDropdown">
                        <button className="dropdown-item" onClick={logout}>Cerrar sesión</button>
                    </div>
                </div>
            }
        </>
    );
}