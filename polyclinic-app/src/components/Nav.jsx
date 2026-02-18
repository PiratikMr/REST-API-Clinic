import React from 'react'
import { Link } from 'react-router-dom'

export default function Nav({ user, onLogout }){
  return (
    <header className="nav">
      <div style={{display:'flex',alignItems:'center',gap:12}}>
        <div className="logo big">Poli</div>
        <div className="brand">Polyclinic</div>
      </div>
      <nav>
        {user ? (
          <>
            <span className="nav-welcome">Привет, {user.login || user.name || 'пользователь'}</span>
            {user.role === 'ADMIN' && <Link to="/admin">Панель</Link>}
            {user.role === 'DOCTOR' && <Link to="/doctor">Доктор</Link>}
            {user.role === 'PATIENT' && <Link to="/patient">Пациент</Link>}
            <button className="btn small" onClick={onLogout}>Выйти</button>
          </>
        ) : (
          <>
            <Link to="/login">Вход</Link>
            <Link to="/register">Регистрация</Link>
          </>
        )}
      </nav>
    </header>
  )
}
