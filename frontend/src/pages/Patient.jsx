import React, { useEffect, useState } from 'react'
import { patients, appointments, doctors } from '../api'

export default function Patient({ user }) {
  const [patientData, setPatientData] = useState(null)
  const [loading, setLoading] = useState(!!user)
  const [isEditing, setIsEditing] = useState(false)
  
  const [isAppointmentModalOpen, setAppointmentModalOpen] = useState(false)

  const [form, setForm] = useState({}) 

  useEffect(() => { 
    if (user?.id) {
      loadData() 
    } else {
      setLoading(false)
    }
  }, [user])

  const loadData = async () => {
    setLoading(true)
    try {
      const res = await patients.get(user.id) 
      setPatientData(res.data)
      
      setForm({
        firstName: res.data.firstName || '',
        lastName: res.data.lastName || '',
        middleName: res.data.middleName || '',
        phoneNumber: res.data.phoneNumber || '',
        address: res.data.address || '',
        gender: res.data.gender || 'MALE',
        birthDate: res.data.birthDate ? res.data.birthDate.split('T')[0] : '',
        snils: res.data.snils || '',
        insurancePolicy: res.data.insurancePolicy || '',
        bloodType: res.data.medicalCard?.bloodType || '',
        rhFactor: res.data.medicalCard?.rhFactor || '',
        allergies: res.data.medicalCard?.allergies || '',
        chronicDiseases: res.data.medicalCard?.chronicDiseases || ''
      })
    } catch (e) { 
      console.error(e) 
    } finally {
      setLoading(false)
    }
  }

  const handleSaveProfile = async (e) => {
    e.preventDefault()
    try {
      const payload = {
        firstName: form.firstName,
        lastName: form.lastName,
        middleName: form.middleName,
        phoneNumber: form.phoneNumber,
        address: form.address,
        gender: form.gender,
        birthDate: form.birthDate ? new Date(form.birthDate).toISOString() : null,
        snils: form.snils,
        insurancePolicy: form.insurancePolicy
      }
      
      await patients.update(user.id, payload)
      setIsEditing(false)
      loadData()
    } catch (e) { 
      alert('Ошибка сохранения профиля') 
    }
  }

  if (loading) return <div className="panel">Загрузка...</div>

  return (
    <div className="panel">
      <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:30}}>
         <div>
            <h2>Личный кабинет</h2>
            {/* <p style={{color:'#6b7280', marginTop:-15}}>
               Пациент: {patientData?.lastName} {patientData?.firstName}
            </p> */}
         </div>
         <div style={{display:'flex', gap:10}}>
             <button className="btn secondary" onClick={() => setIsEditing(!isEditing)}>
                {isEditing ? 'Отменить' : 'Редактировать профиль'}
             </button>
             <button className="btn" onClick={() => setAppointmentModalOpen(true)}>
                + Записаться к врачу
             </button>
         </div>
      </div>

      {isEditing ? (
        <form onSubmit={handleSaveProfile} className="card">
          <h4>Личная информация</h4>
          <div className="form-grid" style={{gridTemplateColumns:'1fr 1fr 1fr'}}>
             <div><label>Фамилия</label><input value={form.lastName} onChange={e=>setForm({...form, lastName:e.target.value})} /></div>
             <div><label>Имя</label><input value={form.firstName} onChange={e=>setForm({...form, firstName:e.target.value})} /></div>
             <div><label>Отчество</label><input value={form.middleName} onChange={e=>setForm({...form, middleName:e.target.value})} /></div>
          </div>
          <div className="form-grid">
             <div><label>Дата рождения</label><input type="date" value={form.birthDate} onChange={e=>setForm({...form, birthDate:e.target.value})} /></div>
             <div><label>Телефон</label><input value={form.phoneNumber} onChange={e=>setForm({...form, phoneNumber:e.target.value})} /></div>
             <div>
                <label>Пол</label>
                <select value={form.gender} onChange={e=>setForm({...form, gender:e.target.value})}>
                   <option value="MALE">Мужской</option>
                   <option value="FEMALE">Женский</option>
                </select>
             </div>
          </div>
          <div><label>Адрес</label><input value={form.address} onChange={e=>setForm({...form, address:e.target.value})} /></div>
          
          <h4 style={{marginTop:20}}>Документы</h4>
          <div className="form-grid">
             <div><label>СНИЛС</label><input value={form.snils} onChange={e=>setForm({...form, snils:e.target.value})} /></div>
             <div><label>Полис ОМС</label><input value={form.insurancePolicy} onChange={e=>setForm({...form, insurancePolicy:e.target.value})} /></div>
          </div>

          <h4 style={{marginTop:20, color:'#6b7280'}}>
             Медицинские данные <span style={{fontSize:12, fontWeight:'normal'}}>(Заполняется врачом)</span>
          </h4>
          <div className="form-grid" style={{opacity:0.7}}>
             <div>
                <label>Группа крови</label>
                <input value={form.bloodType} disabled style={{cursor:'not-allowed', background:'#f3f4f6'}} />
             </div>
             <div>
                <label>Резус-фактор</label>
                <input value={form.rhFactor} disabled style={{cursor:'not-allowed', background:'#f3f4f6'}} />
             </div>
          </div>
          <div style={{opacity:0.7}}>
             <label>Аллергии</label>
             <textarea value={form.allergies} disabled style={{cursor:'not-allowed', background:'#f3f4f6'}} />
          </div>
          <div style={{opacity:0.7}}>
             <label>Хронические заболевания</label>
             <textarea value={form.chronicDiseases} disabled style={{cursor:'not-allowed', background:'#f3f4f6'}} />
          </div>
          
          <button className="btn" style={{marginTop:20}}>Сохранить изменения</button>
        </form>
      ) : (
        <div style={{display:'flex', gap:20, flexWrap:'wrap'}}>
           <div className="card" style={{flex:1, minWidth:300}}>
              <h4>Профиль</h4>
               <p><strong>ФИО:</strong> {`${form.lastName || '-'} ${form.firstName || '-'} ${form.middleName || '-'}`}</p>
              <p><strong>Дата рождения:</strong> {form.birthDate || '-'}</p>
              <p><strong>Телефон:</strong> {form.phoneNumber || '-'}</p>
              <p><strong>Адрес:</strong> {form.address || '-'}</p>
              <div style={{marginTop:15, padding:10, background:'#f9fafb', borderRadius:6}}>
                 <div><strong>СНИЛС:</strong> {form.snils || '-'}</div>
                 <div><strong>Полис:</strong> {form.insurancePolicy || '-'}</div>
              </div>
           </div>
           
           <div className="card" style={{flex:1, minWidth:300, borderTop:'4px solid #ef4444'}}>
              <h4>Медицинская карта</h4>
              <p><strong>Пол:</strong> {form.gender || '-'}</p>
              <div style={{display:'flex', gap:20, marginBottom:15}}>
                 <div><small>Кровь</small><div style={{fontSize:18, fontWeight:'bold'}}>{form.bloodType || '?'}</div></div>
                 <div><small>Резус</small><div style={{fontSize:18, fontWeight:'bold'}}>{form.rhFactor || '?'}</div></div>
              </div>
              <p><strong>Аллергии:</strong> {form.allergies || 'Нет'}</p>
              <p><strong>Хронические заболевания:</strong> {form.chronicDiseases || 'Нет'}</p>
           </div>
        </div>
      )}

      <h3 style={{marginTop:30}}>Мои записи</h3>
      <PatientAppointments patientId={user.id} />

      {isAppointmentModalOpen && (
        <CreateAppointmentModal 
           patientId={user.id} 
           onClose={() => setAppointmentModalOpen(false)} 
        />
      )}
    </div>
  )
}

function CreateAppointmentModal({ patientId, onClose }) {
  const [step, setStep] = useState(1)
  const [doctorsList, setDoctorsList] = useState([])
  const [selectedDoctor, setSelectedDoctor] = useState(null)
  
  const [loadingSlots, setLoadingSlots] = useState(false)
  const [slotsByDate, setSlotsByDate] = useState({})
  const [selectedDate, setSelectedDate] = useState(null)
  const [selectedSlot, setSelectedSlot] = useState(null)

  useEffect(() => {
     doctors.list({ size: 100 })
        .then(res => setDoctorsList(res.data.data || res.data.content || [])) 
        .catch(err => console.error(err))
  }, [])

  const handleDoctorSelect = async (doc) => {
     setSelectedDoctor(doc)
     setLoadingSlots(true)
     setStep(2)
     setSlotsByDate({})
     setSelectedDate(null)
     setSelectedSlot(null)

     try {
        const res = await doctors.detail(doc.id)
        const schedules = res.data.schedules || []
        
        const grouped = {}
        schedules.forEach(slot => {
            const dateKey = slot.startTime.split('T')[0]
            if (!grouped[dateKey]) grouped[dateKey] = []
            grouped[dateKey].push(slot)
        })

        Object.keys(grouped).forEach(date => {
            grouped[date].sort((a, b) => new Date(a.startTime) - new Date(b.startTime))
        })

        setSlotsByDate(grouped)
        const dates = Object.keys(grouped).sort()
        if (dates.length > 0) setSelectedDate(dates[0])

     } catch (e) {
        console.error(e)
        alert('Не удалось загрузить расписание врача')
     } finally {
        setLoadingSlots(false)
     }
  }

  const submit = async () => {
     if(!selectedSlot) return alert('Выберите время записи')
     
     const payload = {
         patientId: patientId, 
         doctorSlotId: selectedSlot.id 
     }

     try {
       await appointments.create(payload)
       alert('Запись успешно создана!')
       window.location.reload()
     } catch(e) {
       console.error(e)
       alert('Ошибка: ' + (e.response?.data?.message || 'Не удалось создать запись'))
     }
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content" style={{maxWidth:600}}>
        <div className="modal-header">
           <h3>Запись на прием {step === 2 && ' - Выбор времени'}</h3>
           <button className="close-btn" onClick={onClose}>✕</button>
        </div>
        
        {step === 1 && (
          <div>
            <label>Выберите врача:</label>
            {doctorsList.length === 0 && <p style={{color:'#666'}}>Загрузка...</p>}
            <ul className="dict-list" style={{maxHeight:400, overflowY:'auto'}}>
               {doctorsList.map(d => (
                 <li key={d.id} style={{cursor:'pointer', display:'flex', justifyContent:'space-between'}} 
                     onClick={() => handleDoctorSelect(d)}>
                    <div>
                       <strong>{d.lastName} {d.firstName}</strong>
                       <div style={{color:'#666', fontSize:12}}>{d.specialty}</div>
                    </div>
                    <button className="btn small secondary">→</button>
                 </li>
               ))}
            </ul>
          </div>
        )}

        {step === 2 && (
          <div>
             <div style={{background:'#f3f4f6', padding:10, borderRadius:6, marginBottom:15, display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                <div>
                    <strong>Врач:</strong> {selectedDoctor.lastName} {selectedDoctor.firstName}
                    <br/><small>{selectedDoctor.specialty}</small>
                </div>
                <button className="btn small secondary" onClick={()=>setStep(1)}>Сменить</button>
             </div>
             
             {loadingSlots ? (
                <div style={{textAlign:'center', padding:20}}>Загрузка расписания...</div>
             ) : Object.keys(slotsByDate).length === 0 ? (
                <div style={{textAlign:'center', padding:20, color:'#666'}}>Нет свободного времени</div>
             ) : (
                <>
                   <label>Выберите дату:</label>
                   <div style={{display:'flex', gap:8, overflowX:'auto', paddingBottom:10, marginBottom:15}}>
                      {Object.keys(slotsByDate).sort().map(dateKey => (
                         <button 
                            key={dateKey}
                            className={`btn ${selectedDate === dateKey ? '' : 'secondary'}`}
                            onClick={() => { setSelectedDate(dateKey); setSelectedSlot(null); }}
                         >
                            {new Date(dateKey).toLocaleDateString()}
                         </button>
                      ))}
                   </div>

                   {selectedDate && (
                      <div>
                         <label>Свободное время:</label>
                         <div style={{display:'grid', gridTemplateColumns:'repeat(auto-fill, minmax(80px, 1fr))', gap:10}}>
                            {slotsByDate[selectedDate].map(slot => {
                               const timeString = new Date(slot.startTime).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})
                               const isSelected = selectedSlot?.id === slot.id
                               return (
                                  <button
                                     key={slot.id}
                                     type="button"
                                     onClick={() => setSelectedSlot(slot)}
                                     style={{
                                        padding: '8px',
                                        borderRadius: '6px',
                                        border: isSelected ? '2px solid var(--primary)' : '1px solid #ddd',
                                        background: isSelected ? '#eff6ff' : '#fff',
                                        color: isSelected ? 'var(--primary)' : '#333',
                                        cursor: 'pointer',
                                        fontWeight: isSelected ? 'bold' : 'normal'
                                     }}
                                  >
                                     {timeString}
                                  </button>
                               )
                            })}
                         </div>
                      </div>
                   )}
                   
                   <div style={{marginTop:30, borderTop:'1px solid #eee', paddingTop:20, textAlign:'right'}}>
                      <button className="btn" onClick={submit} disabled={!selectedSlot} style={{opacity: !selectedSlot ? 0.5 : 1}}>
                        Подтвердить запись
                      </button>
                   </div>
                </>
             )}
          </div>
        )}
      </div>
    </div>
  )
}

function PatientAppointments({ patientId }) {
  const [list, setList] = useState([])
  const [selectedAppt, setSelectedAppt] = useState(null)
  
  useEffect(()=>{
     if(patientId) {
        appointments.getForPatient(patientId, { statuses: "ACTIVE,CLOSED,CANCELLED" })
          
          .then(res => setList(res.data.data || res.data.content || [])) 
          .catch(e => console.error("Ошибка загрузки записей:", e))
     }
  },[patientId])

  if(!list.length) return <div className="card" style={{color:'#666', textAlign:'center'}}>Нет активных записей</div>

  return (
    <>
    <div className="card">
       {list.map(a => (
         <div key={a.id} style={{borderBottom:'1px solid #eee', padding:'12px 0', display:'flex', justifyContent:'space-between', alignItems:'center'}}>
            <div>
               <div style={{fontSize:16, fontWeight:600}}>
                  {new Date(a.startTime || a.visitInfo?.time).toLocaleDateString()} {new Date(a.startTime || a.visitInfo?.time).toLocaleTimeString([],{hour:'2-digit',minute:'2-digit'})}
               </div>
               
               {/* вложенный объект doctor  */}
               <div style={{color:'#4b5563'}}>
                  Врач: {a.doctor ? `${a.doctor.lastName} ${a.doctor.firstName}` : 'Не назначен'}
               </div>
               <div style={{color:'#9ca3af', fontSize:13}}>
                  {a.doctor?.specialty} • Кабинет {a.room || a.doctorSlot?.room || a.visitInfo?.room || '—'}
               </div>
               {/* ============================================================ */}

            </div>
            
            <div style={{display:'flex', gap:10, alignItems:'center'}}>
               <span className={`badge-status ${a.status==='ACTIVE' ? 'success' : (a.status==='CLOSED'?'':'secondary')}`} 
                     style={a.status==='CLOSED' ? {background:'#10b981', color:'white'} : {}}>
                  {a.status==='ACTIVE' ? 'Запланировано' : 
                   a.status==='CLOSED' ? 'Завершен' : a.status}
               </span>
               
               {a.status === 'CLOSED' && (
                 <button className="btn small secondary" onClick={() => setSelectedAppt(a)}>Результаты</button>
               )}
            </div>
         </div>
       ))}
    </div>
    
    {selectedAppt && (
       <AppointmentDetailsModal 
         appt={selectedAppt} 
         onClose={() => setSelectedAppt(null)} 
       />
    )}
    </>
  )
}

function AppointmentDetailsModal({ appt, onClose }) {
  const [details, setDetails] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    appointments.detail(appt.id)
      .then(res => setDetails(res.data))
      .catch(e => {
        console.error(e)
        alert('Не удалось загрузить детали приема')
      })
      .finally(() => setLoading(false))
  }, [appt.id])

  return (
    <div className="modal-overlay">
      <div className="modal-content" style={{maxWidth:700}}>
         <div className="modal-header">
            <h3>Результаты приема от {new Date(appt.startTime || appt.visitInfo?.time).toLocaleDateString()}</h3>
            <button className="close-btn" onClick={onClose}>✕</button>
         </div>
         
         {loading ? <div style={{padding:20}}>Загрузка...</div> : details && (
           <div>
              <div style={{background:'#f9fafb', padding:15, borderRadius:8, marginBottom:15}}>
                 { }
                 <p><strong>Врач:</strong> {details.doctor ? `${details.doctor.lastName} ${details.doctor.firstName}` : (details.doctorFullName || '—')}</p>
                 <p><strong>Жалобы:</strong> {details.complaints || '—'}</p>
                 <p style={{fontSize:'1.1rem', color:'#059669'}}><strong>Диагноз:</strong> {details.diagnosis}</p>
              </div>

              {details.medications && details.medications.length > 0 && (
                <div style={{marginBottom:15}}>
                  <h4 style={{marginBottom:5}}>Назначенные лекарства</h4>
                  <ul className="dict-list">
                    {details.medications.map((m, i) => (
                      <li key={i} style={{padding:'6px 10px'}}>
                         <strong>{m.name}</strong> <span style={{color:'#666'}}>— {m.details}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {details.tests && details.tests.length > 0 && (
                <div style={{marginBottom:15}}>
                  <h4 style={{marginBottom:5}}>Анализы</h4>
                  <ul className="dict-list">
                    {details.tests.map((t, i) => (
                      <li key={i} style={{padding:'6px 10px'}}>
                         <strong>{t.name}</strong> 
                         {t.result && <span style={{color:'#666'}}> (Результат: {t.result})</span>}
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {details.procedures && details.procedures.length > 0 && (
                <div style={{marginBottom:15}}>
                  <h4 style={{marginBottom:5}}>Процедуры</h4>
                  <ul className="dict-list">
                    {details.procedures.map((p, i) => (
                      <li key={i} style={{padding:'6px 10px'}}>
                         <strong>{p.name}</strong> <span style={{color:'#666'}}>— {p.sessions} сеанс(ов)</span>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
           </div>
         )}
         
         <div style={{marginTop:20, textAlign:'right'}}>
           <button className="btn" onClick={onClose}>Закрыть</button>
         </div>
      </div>
    </div>
  )
}