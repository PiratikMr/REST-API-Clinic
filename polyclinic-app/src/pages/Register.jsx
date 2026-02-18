import React, { useState } from 'react'
import { auth } from '../api'
import { useNavigate } from 'react-router-dom'

export default function Register(){
  const [form, setForm] = useState({ 
    login:'', 
    password:'', 
    firstName:'', 
    lastName:'', 
    middleName:'',
    address:'' 
  })
  const [error, setError] = useState(null)
  const [success, setSuccess] = useState(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setError(null)
    setSuccess(null)
    setLoading(true)
    
    if (!form.login || !form.password || !form.firstName || !form.lastName || !form.address) {
      setError('Заполните все обязательные поля')
      setLoading(false)
      return
    }
    
    try{
      await auth.register(form)
      setSuccess('Регистрация успешна! Вы будете перенаправлены на страницу входа.')
      setTimeout(() => {
        navigate('/login')
      }, 2000)
    }catch(err){
      setError(err?.response?.data?.message || err.message || 'Ошибка регистрации')
    }finally{
      setLoading(false)
    }
  }

  return (
    <div className="auth-box">
      <div className="auth-header">
        <div className="logo">Poli</div>
        <h2>Регистрация</h2>
        <p className="small" style={{marginTop:'0.5rem'}}>
          Создайте учетную запись пациента
        </p>
      </div>
      
      <form onSubmit={submit} className="auth-form">
        <div style={{display:'grid',gridTemplateColumns:'1fr 1fr',gap:'1rem'}}>
          <label>
            Имя *
            <input 
              placeholder="Введите имя" 
              value={form.firstName} 
              onChange={e=>setForm({...form, firstName:e.target.value})} 
              required
            />
          </label>
          
          <label>
            Фамилия *
            <input 
              placeholder="Введите фамилию" 
              value={form.lastName} 
              onChange={e=>setForm({...form, lastName:e.target.value})} 
              required
            />
          </label>
        </div>
        
        <label>
          Отчество
          <input 
            placeholder="Введите отчество (необязательно)" 
            value={form.middleName} 
            onChange={e=>setForm({...form, middleName:e.target.value})} 
          />
        </label>
        
        <label>
          Адрес *
          <input 
            placeholder="Введите адрес проживания" 
            value={form.address} 
            onChange={e=>setForm({...form, address:e.target.value})} 
            required
          />
        </label>
        
        <label>
          Логин *
          <input 
            placeholder="Придумайте логин" 
            value={form.login} 
            onChange={e=>setForm({...form, login:e.target.value})} 
            required
          />
        </label>
        
        <label>
          Пароль *
          <input 
            type="password" 
            placeholder="Придумайте пароль" 
            value={form.password} 
            onChange={e=>setForm({...form, password:e.target.value})} 
            required
          />
        </label>
        
        <button className="btn" type="submit" disabled={loading} style={{marginTop:'1rem'}}>
          {loading ? 'Регистрация...' : 'Зарегистрироваться'}
        </button>
      </form>
      
      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}
      
      <div className="small" style={{textAlign:'center',marginTop:'2rem'}}>
        Уже есть аккаунт?{' '}
        <a onClick={()=>navigate('/login')} style={{color:'#667eea',cursor:'pointer',textDecoration:'underline'}}>
          Войти
        </a>
      </div>
    </div>
  )
}