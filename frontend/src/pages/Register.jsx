import React, { useState } from 'react'
import { auth } from '../api'
import { useNavigate } from 'react-router-dom'

export default function Register() {
  const navigate = useNavigate()
  
  const [form, setForm] = useState({
    username: '', password: '', firstName: '', lastName: '', middleName: ''
  })
  
  // Состояние для ошибок
  const [errors, setErrors] = useState({})

  // Функция валидации
  const validate = () => {
    const newErrors = {}
    const nameRegex = /^[а-яА-ЯёЁa-zA-Z\s-]+$/ // Только буквы, пробел и дефис

    // 1. Логин
    if (!form.username.trim()) {
      newErrors.username = 'Логин обязателен'
    } else if (form.username.length < 4) {
      newErrors.username = 'Минимум 4 символа'
    }

    // 2. Пароль
    if (!form.password) {
      newErrors.password = 'Пароль обязателен'
    } else if (form.password.length < 6) {
      newErrors.password = 'Минимум 6 символов'
    }

    // 3. Имя (разрешаем 1 букву, но запрещаем цифры/символы)
    if (!form.firstName.trim()) {
      newErrors.firstName = 'Имя обязательно'
    } else if (!nameRegex.test(form.firstName)) {
      newErrors.firstName = 'Имя должно содержать только буквы'
    }

    // 4. Фамилия
    if (!form.lastName.trim()) {
      newErrors.lastName = 'Фамилия обязательна'
    } else if (!nameRegex.test(form.lastName)) {
      newErrors.lastName = 'Фамилия должна содержать только буквы'
    }

    // 5. Отчество (не обязательно, но если ввели - только буквы)
    if (form.middleName && !nameRegex.test(form.middleName)) {
      newErrors.middleName = 'Отчество должно содержать только буквы'
    }

    setErrors(newErrors)
    // Если ключей в объекте ошибок нет (length === 0), значит всё ок
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    // Сначала проверяем
    if (!validate()) return

    try {
      await auth.register(form)
      alert('Регистрация успешна! Теперь войдите.')
      navigate('/login')
    } catch (e) {
      alert('Ошибка: ' + (e.response?.data?.message || 'Не удалось зарегистрироваться'))
    }
  }

  // Стиль для ошибок
  const errorStyle = { color: '#dc2626', fontSize: '0.8rem', marginTop: '4px', display: 'block' }
  const inputErrorStyle = { borderColor: '#dc2626', background: '#fef2f2' }

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Регистрация</h2>
        
        <div style={{marginBottom:15}}>
           <input 
             placeholder="Логин *" 
             value={form.username} 
             onChange={e=>setForm({...form, username:e.target.value})} 
             style={errors.username ? inputErrorStyle : {}}
           />
           {errors.username && <span style={errorStyle}>{errors.username}</span>}
        </div>

        <div style={{marginBottom:15}}>
           <input 
             type="password" 
             placeholder="Пароль *" 
             value={form.password} 
             onChange={e=>setForm({...form, password:e.target.value})} 
             style={errors.password ? inputErrorStyle : {}}
           />
           {errors.password && <span style={errorStyle}>{errors.password}</span>}
        </div>

        <div style={{marginBottom:15}}>
           <input 
             placeholder="Имя *" 
             value={form.firstName} 
             onChange={e=>setForm({...form, firstName:e.target.value})} 
             style={errors.firstName ? inputErrorStyle : {}}
           />
           {errors.firstName && <span style={errorStyle}>{errors.firstName}</span>}
        </div>

        <div style={{marginBottom:15}}>
           <input 
             placeholder="Фамилия *" 
             value={form.lastName} 
             onChange={e=>setForm({...form, lastName:e.target.value})} 
             style={errors.lastName ? inputErrorStyle : {}}
           />
           {errors.lastName && <span style={errorStyle}>{errors.lastName}</span>}
        </div>

        <div style={{marginBottom:20}}>
           <input 
             placeholder="Отчество (если есть)" 
             value={form.middleName} 
             onChange={e=>setForm({...form, middleName:e.target.value})} 
             style={errors.middleName ? inputErrorStyle : {}}
           />
           {errors.middleName && <span style={errorStyle}>{errors.middleName}</span>}
        </div>

        <button className="btn full">Зарегистрироваться</button>
        <p style={{textAlign:'center', marginTop:15}}>
           Уже есть аккаунт? <a href="/login">Войти</a>
        </p>
      </form>
    </div>
  )
}