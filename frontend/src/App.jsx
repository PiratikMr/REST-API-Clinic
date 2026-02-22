import React, { useState, useEffect } from 'react'
import { Routes, Route, useNavigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Admin from './pages/Admin'
import Doctor from './pages/Doctor'
import Patient from './pages/Patient'
import Nav from './components/Nav'

function App(){
  const normalizeRole = (r) => {
    if (!r) return r
    if (typeof r !== 'string') return r
    let role = r.trim().toUpperCase()
    if (role.startsWith('ROLE_')) role = role.replace(/^ROLE_/, '')
    return role
  }

  const [user, setUser] = useState(() => {
    try {
      const raw = JSON.parse(localStorage.getItem('user'))
      if (raw && raw.role) raw.role = normalizeRole(raw.role)
      return raw
    } catch { return null }
  })
  const navigate = useNavigate()

  useEffect(() => {
    if (!user) {
      const path = window.location.pathname
      if (path !== '/login' && path !== '/register') navigate('/login')
    }
  }, [user])

  const onLogin = (userObj, token) => {
    // normalize role coming from API
    if (userObj && userObj.role) userObj.role = normalizeRole(userObj.role)
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(userObj))
    setUser(userObj)
    // navigate based on role
    if (userObj?.role === 'ADMIN') navigate('/admin')
    else if (userObj?.role === 'DOCTOR') navigate('/doctor')
    else navigate('/patient')
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
    navigate('/login')
  }

  return (
    <div className="app-root">
      <Nav user={user} onLogout={logout} />
      <main>
        <Routes>
          <Route path="/login" element={<Login onLogin={onLogin} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/admin" element={<Admin user={user} />} />
          <Route path="/doctor" element={<Doctor user={user} />} />
          <Route path="/patient" element={<Patient user={user} />} />
          <Route path="/" element={<Login onLogin={onLogin} />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
