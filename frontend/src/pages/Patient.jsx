import React, { useEffect, useState } from 'react'
import { doctors, appointments, patients } from '../api'

export default function Patient({ user }){
  const [docs, setDocs] = useState([])
  const [myAppts, setMyAppts] = useState([])
  const [openSlots, setOpenSlots] = useState({})
  const [loading, setLoading] = useState(false)
  const [filterStatus, setFilterStatus] = useState(['ACTIVE', 'CLOSED', 'CANCELLED'])
  const [appointmentDetails, setAppointmentDetails] = useState({})

  
  const [isEditProfileOpen, setEditProfileOpen] = useState(false)
  const [profileForm, setProfileForm] = useState({ firstName: '', lastName: '', middleName: '', address: '' })
  const [patientData, setPatientData] = useState(null)
  useEffect(()=>{ 
    if (user?.id) {
      loadDoctors(); 
      loadMyAppointments() 
      loadPatientData();
    }
  }, [user])

  async function loadDoctors(){
    setLoading(true)
    try{
      const res = await doctors.list({ page: 0 })
      setDocs(res.data?.content || res.data || [])
    }catch(e){ 
      console.error(e)
      alert('Ошибка при загрузке врачей')
    }
    setLoading(false)
  }

  async function loadPatientData() {
    try {
      const res = await patients.get(user.id)
      setPatientData(res.data)
      console.log('Patient data loaded:', res.data)
    } catch(e) {
      console.error('Error loading patient data:', e)
      
      setPatientData(user)
    }
  }
  
  async function loadMyAppointments(){
    try{
      const res = await appointments.getForPatient(user?.id || 0, { 
        statuses: filterStatus,
        page: 0 
      })
      setMyAppts(res.data?.content || res.data || [])
    }catch(e){ 
      console.error(e)
    }
  }

  const loadSlots = async (docId) => {
    try{
      const res = await doctors.detail(docId)
      const data = res.data || {}
      const slots = data.schedules || data.slots || []
      setOpenSlots(prev => ({ 
        ...prev, 
        [docId]: openSlots[docId] ? null : slots 
      }))
    }catch(e){ 
      console.error(e)
    }
  }

  const loadAppointmentDetails = async (appointmentId) => {
    try{
      const res = await appointments.detail(appointmentId)
      setAppointmentDetails(prev => ({
        ...prev,
        [appointmentId]: res.data
      }))
    }catch(e){ console.error(e) }
  }

  const bookSlot = async (slotId, doctorId) => {
    if (!window.confirm('Записаться на этот слот?')) return
    try{
      await appointments.create({ patientId: user.id, doctorSlotId: slotId })
      loadMyAppointments()
      setOpenSlots(prev => {
        const copy = { ...prev }
        if (copy[doctorId]) copy[doctorId] = copy[doctorId].filter(s => s.id !== slotId)
        return copy
      })
      alert('✅ Запись создана!')
    }catch(e){
      alert('❌ Ошибка: ' + (e?.response?.data?.message || e.message))
    }
  }

  const cancelAppointment = async (appointmentId) => {
    if (!window.confirm('Отменить запись?')) return
    try{
      await appointments.cancel(appointmentId)
      loadMyAppointments()
      alert('✅ Запись отменена')
    }catch(e){ alert('Ошибка отмены') }
  }

  
   const handleEditProfileClick = () => {
    // Используем данные пациента, если они есть, иначе данные из user
    const data = patientData || user
    setProfileForm({
      firstName: data.firstName || '',
      lastName: data.lastName || '',
      middleName: data.middleName || '',
      address: data.address || ''
    })
    setEditProfileOpen(true)
  }

  const handleProfileSubmit = async (e) => {
    e.preventDefault()
    try {
      await patients.update(user.id, profileForm)
      alert('Данные обновлены!')
      setEditProfileOpen(false)
      // Перезагружаем данные пациента после обновления
      await loadPatientData()
      // Обновляем user, если он передается через пропсы
      if (typeof window !== 'undefined') {
        const updatedUser = JSON.parse(localStorage.getItem('user') || '{}')
        Object.assign(updatedUser, profileForm)
        localStorage.setItem('user', JSON.stringify(updatedUser))
      }
    } catch(e) {
      alert('Ошибка обновления: ' + (e.response?.data?.message || e.message))
    }
  }
  
  const formatDateTime = (dt) => {
    if (!dt) return '—'
    try { return new Date(dt).toLocaleString('ru-RU', {day:'2-digit', month:'2-digit', year:'numeric', hour:'2-digit', minute:'2-digit'}) } 
    catch { return dt }
  }

  const formatTimeRange = (start, end) => {
    try {
      const s = new Date(start).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})
      const e = new Date(end).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})
      return `${s} - ${e}`
    } catch { return '' }
  }

  const formatDateOnly = (dt) => {
    try { return new Date(dt).toLocaleDateString('ru-RU', {weekday:'short', day:'numeric', month:'long'}) } 
    catch { return dt }
  }

  return (
    <div className="panel">
      {/* Профиль пациента - теперь используем patientData */}
      <div style={{
        display:'flex', 
        justifyContent:'space-between', 
        alignItems:'flex-start', 
        marginBottom:25, 
        paddingBottom:15, 
        borderBottom:'1px solid #e2e8f0'
      }}>
        <div>
          <h2 style={{margin:'0 0 8px 0', fontSize:'1.8rem'}}>
            👤 {patientData?.lastName || user?.lastName || ''} {patientData?.firstName || user?.firstName || ''} {patientData?.middleName || user?.middleName || ''}
          </h2>
          <div style={{color:'#4a5568', fontSize:'1rem'}}>
             <strong>Адрес проживания:</strong> {patientData?.address || user?.address || <span style={{color:'#999', fontStyle:'italic'}}>Не указан</span>}
          </div>
        </div>
        <button className="btn secondary small" onClick={handleEditProfileClick} style={{marginTop:5}}>
          ✎ Ред. профиль
        </button>
      </div>
      
      <div className="row">
        {}
        <div className="card" style={{flex:1}}>
          <h3>Запись к врачу</h3>
          {loading && <div className="loading-bar">Загрузка списка врачей...</div>}
          
          <div style={{display:'flex', flexDirection:'column', gap:15}}>
            {docs.map(d => (
              <div key={d.id} style={{border:'1px solid #eee', borderRadius:8, padding:15}}>
                <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                  <div>
                    <div style={{fontWeight:600, fontSize:'1.1rem'}}>{d.lastName} {d.firstName} {d.middleName}</div>
                    <div className="badge-primary">{d.specialty}</div>
                  </div>
                  <button className="btn small secondary" onClick={()=>loadSlots(d.id)}>
                    {openSlots[d.id] ? 'Скрыть слоты ▲' : 'Расписание ▼'}
                  </button>
                </div>

                {openSlots[d.id] && (
                  <div style={{marginTop:15, borderTop:'1px solid #f0f0f0', paddingTop:10}}>
                    {openSlots[d.id].length === 0 ? <div className="small">Нет свободных талонов</div> : (
                      <div style={{display:'grid', gridTemplateColumns:'repeat(auto-fill, minmax(220px, 1fr))', gap:10}}>
                        {openSlots[d.id].map(slot => (
                          <div key={slot.id} className="time-slot" style={{display:'block'}}>
                            <div style={{fontWeight:600}}>{formatDateOnly(slot.startTime)}</div>
                            <div style={{color:'#4338ca'}}>{formatTimeRange(slot.startTime, slot.endTime)}</div>
                            <div className="small" style={{marginTop:4, color:'#666'}}>
                              📍 Каб. {slot.room} {slot.district ? `• Уч. ${slot.district}` : ''}
                            </div>
                            <button className="btn small primary" style={{marginTop:8, width:'100%'}} onClick={()=>bookSlot(slot.id, d.id)}>
                              Записаться
                            </button>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {}
        <div className="card" style={{width:'450px'}}>
          <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
            <h3> Мои визиты</h3>
            <button className="btn small" onClick={loadMyAppointments}>↻</button>
          </div>
          
          <div style={{display:'flex', gap:5, marginBottom:15, flexWrap:'wrap'}}>
             {['ACTIVE', 'CLOSED', 'CANCELLED'].map(st => (
               <button key={st} 
                 className={`btn small ${filterStatus.includes(st) ? '' : 'secondary'}`}
                 onClick={() => setFilterStatus(prev => prev.includes(st) ? prev.filter(x=>x!==st) : [...prev, st])}
               >
                 {st === 'ACTIVE' ? 'Активные' : st === 'CLOSED' ? 'Архив' : 'Отмена'}
               </button>
             ))}
          </div>
          
          {myAppts.length === 0 ? <div className="empty-state">Список пуст</div> : (
            <div style={{display:'flex', flexDirection:'column', gap:10}}>
              {myAppts.map(a => {
                 const details = appointmentDetails[a.id]
                 return (
                  <div key={a.id} className="card" style={{padding:12, background: a.status==='CANCELLED'?'#fff5f5': a.status==='CLOSED'?'#f8fafc':'#fff'}}>
                    <div style={{display:'flex', justifyContent:'space-between'}}>
                      <div style={{fontWeight:600, color: a.status==='ACTIVE'?'#4338ca':'#666'}}>
                        {formatDateTime(a.visitInfo.time)}
                      </div>
                      <span className={`badge status-${a.status}`} style={{fontSize:'0.7rem'}}>{a.status}</span>
                    </div>
                    
                    <div style={{marginTop:5}}>
                       Врач: <strong>{a.doctor.lastName} {a.doctor.firstName}</strong>
                       <div className="small" style={{color:'#666'}}>{a.doctor.specialty}</div>
                       <div className="small" style={{color:'#666', marginTop:3}}>
                          📍 Кабинет: {a.visitInfo.room} {a.visitInfo.district ? `• Уч. ${a.visitInfo.district}` : ''}
                       </div>
                    </div>

                    {}
                    <div style={{marginTop:10, display:'flex', gap:10}}>
                      {a.status === 'ACTIVE' && new Date(a.visitInfo.time) > new Date() && (
                        <button className="btn small danger" onClick={()=>cancelAppointment(a.id)}>Отменить</button>
                      )}
                      {a.status === 'CLOSED' && !details && (
                        <button className="btn small secondary" onClick={()=>loadAppointmentDetails(a.id)}>Детали приема</button>
                      )}
                    </div>

                    {}
                    {details && (
                      <div style={{marginTop:10, paddingTop:10, borderTop:'1px dashed #ccc', fontSize:'0.9rem'}}>
                        <div><strong>Дз:</strong> {details.diagnosis}</div>
                        {details.medications?.length > 0 && (
                          <div style={{marginTop:5}}> {details.medications.map(m=>m.name).join(', ')}</div>
                        )}
                        {details.tests?.length > 0 && (
                          <div style={{marginTop:5}}> {details.tests.map(t=>`${t.name} (${t.result||'-'})`).join(', ')}</div>
                        )}
                      </div>
                    )}
                  </div>
                 )
              })}
            </div>
          )}
        </div>
      </div>

      {}
      {isEditProfileOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Редактирование профиля</h3>
              <button className="close-btn" onClick={()=>setEditProfileOpen(false)}>✕</button>
            </div>
            <form onSubmit={handleProfileSubmit}>
              <div className="form-grid">
                <div>
                   <label>Имя</label>
                   <input value={profileForm.firstName} onChange={e=>setProfileForm({...profileForm, firstName:e.target.value})} required/>
                </div>
                <div>
                   <label>Фамилия</label>
                   <input value={profileForm.lastName} onChange={e=>setProfileForm({...profileForm, lastName:e.target.value})} required/>
                </div>
              </div>
              <label>Отчество</label>
              <input value={profileForm.middleName} onChange={e=>setProfileForm({...profileForm, middleName:e.target.value})} />
              
              <label>Адрес проживания</label>
              <input value={profileForm.address} onChange={e=>setProfileForm({...profileForm, address:e.target.value})} />

              <div style={{marginTop:20, display:'flex', gap:10}}>
                <button className="btn" style={{flex:1}}>Сохранить</button>
                <button type="button" className="btn secondary" onClick={()=>setEditProfileOpen(false)}>Отмена</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}