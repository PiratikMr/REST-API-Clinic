import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { auth } from '../api'

export default function Login({ onLogin }){
  const [login, setLogin] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await auth.login({ login, password })
      const data = res.data
      
      // Бэкенд возвращает { token, id, username, role } в JwtResponse
      // Но в Login.jsx была логика обработки разных форматов. Упростим под реальный ответ бэка.
      const token = data.token
      const user = { 
        id: data.id, 
        login: data.username, 
        role: data.role // Уже содержит строку типа "ROLE_ADMIN" или просто "ADMIN"
      }

      if (onLogin) onLogin(user, token)
    } catch(err) {
      setError(err?.response?.data?.message || 'Ошибка входа')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-box card">
      <div className="auth-header">
        <h2 style={{color:'var(--primary)'}}>PoliClinic</h2>
        <p className="small">Вход в систему</p>
      </div>
      <form onSubmit={submit}>
        <label>Логин</label>
        <input value={login} onChange={e=>setLogin(e.target.value)} required />
        <label>Пароль</label>
        <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required />
        <button className="btn" disabled={loading} style={{width:'100%', marginTop:20}}>
          {loading ? 'Входим...' : 'Войти'}
        </button>
      </form>
      {error && <div className="error">{error}</div>}
      <div style={{textAlign:'center', marginTop:20}}>
         <span className="small link" onClick={()=>navigate('/register')} style={{cursor:'pointer', color:'var(--primary)'}}>
           Регистрация пациента
         </span>
      </div>
    </div>
  )
}