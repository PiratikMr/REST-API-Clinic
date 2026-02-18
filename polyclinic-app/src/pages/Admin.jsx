import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dictionaries, doctors } from '../api'
import { groupSlotsToRanges, formatDisplayDate, toServerDate } from '../utils/dateHelper'

export default function Admin({ user }) {
  const navigate = useNavigate()
  const [tab, setTab] = useState('doctors')
  const [loading, setLoading] = useState(false)

  
  const [doctorsList, setDoctorsList] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  
  const [viewDoctor, setViewDoctor] = useState(null)
  const [editDoctor, setEditDoctor] = useState(null)
  const [isCreateModalOpen, setCreateModalOpen] = useState(false)

  
  const [dicts, setDicts] = useState({ specialties: [], meds: [], tests: [], procs: [] })

  useEffect(() => {
    
    loadDictionaries()
  }, [])

  useEffect(() => {
    loadDoctors(page)
  }, [page])

  
  const loadDoctors = async (pageNumber) => {
    setLoading(true)
    try {
      const res = await doctors.list({ page: pageNumber, size: 20 })
      setDoctorsList(res.data.content || [])
      setTotalPages(res.data.totalPages)
    } catch (e) {
      console.error(e)
    }
    setLoading(false)
  }

  const loadDictionaries = async () => {
    try {
      const [s, m, t, p] = await Promise.all([
        dictionaries.specialties(),
        dictionaries.medications(),
        dictionaries.tests(),
        dictionaries.procedures()
      ])
      setDicts({ specialties: s.data, meds: m.data, tests: t.data, procs: p.data })
    } catch (e) { console.error(e) }
  }

  
  const fetchDetails = async (id) => {
    try {
      const res = await doctors.detail(id)
      return res.data
    } catch (e) {
      alert('Ошибка загрузки данных врача')
      return null
    }
  }

  const handleView = async (id) => {
    setLoading(true)
    const data = await fetchDetails(id)
    if (data) setViewDoctor(data)
    setLoading(false)
  }

  const handleEdit = async (id) => {
    setLoading(true)
    const data = await fetchDetails(id)
    if (data) setEditDoctor(data)
    setLoading(false)
  }

 
  const getDailySchedule = (rawSchedules) => {
    const groupedByDay = {}
    
    const ranges = groupSlotsToRanges(rawSchedules)

    ranges.forEach(range => {
      const dateKey = new Date(range.startTime).toLocaleDateString('ru-RU', {
        weekday: 'short', day: 'numeric', month: 'long'
      })
      if (!groupedByDay[dateKey]) groupedByDay[dateKey] = []
      groupedByDay[dateKey].push(range)
    })
    return groupedByDay
  }

  return (
    <div className="panel">
      <div className="admin-header">
        <h2>Панель управления</h2>
        <div className="tabs">
          <button className={`tab-btn ${tab === 'doctors' ? 'active' : ''}`} onClick={() => setTab('doctors')}>Врачи</button>
          <button className={`tab-btn ${tab === 'catalogs' ? 'active' : ''}`} onClick={() => setTab('catalogs')}>Справочники</button>
        </div>
      </div>

      {loading && <div className="loading-bar">Загрузка...</div>}

      {}
      {tab === 'doctors' && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 20 }}>
            <h3>Список персонала</h3>
            <button className="btn" onClick={() => setCreateModalOpen(true)}>+ Новый врач</button>
          </div>

          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>ФИО</th>
                  <th>Специальность</th>
                  <th style={{ textAlign: 'right' }}>Действия</th>
                </tr>
              </thead>
              <tbody>
                {doctorsList.map(d => (
                  <tr key={d.id}>
                    <td>
                      <div style={{ fontWeight: 600 }}>{d.lastName} {d.firstName} {d.middleName}</div>
                    </td>
                    <td><span className="badge-gray">{d.specialty || '—'}</span></td>
                    <td style={{ textAlign: 'right' }}>
                      <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                        <button className="btn small secondary" onClick={() => handleView(d.id)}>📅 График</button>
                        <button className="btn small" onClick={() => handleEdit(d.id)}>✎ Ред.</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {}
          <div style={{ marginTop: 20, display: 'flex', justifyContent: 'center', gap: 10, alignItems: 'center' }}>
            <button className="btn secondary small" disabled={page === 0} onClick={() => setPage(p => p - 1)}>← Назад</button>
            <span>Страница {page + 1} из {totalPages || 1}</span>
            <button className="btn secondary small" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Вперед →</button>
          </div>
        </div>
      )}

      {}
      {tab === 'catalogs' && <DictionariesView dicts={dicts} reload={loadDictionaries} />}

      {}
      {viewDoctor && (
        <div className="modal-overlay" onClick={() => setViewDoctor(null)}>
          <div className="modal-content large" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Карточка врача</h3>
              <button className="close-btn" onClick={() => setViewDoctor(null)}>✕</button>
            </div>
            
            <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
              <div>
                <h2>{viewDoctor.lastName} {viewDoctor.firstName} {viewDoctor.middleName}</h2>
                <div className="badge-primary large">{viewDoctor.specialty}</div>
              </div>
            </div>

            <div className="divider">Рабочий график (Смены)</div>
            
            {!viewDoctor.schedules?.length ? (
              <div className="empty-state">Расписание не задано</div>
            ) : (
              <div className="calendar-grid">
                {Object.entries(getDailySchedule(viewDoctor.schedules)).map(([date, ranges]) => (
                  <div key={date} className="calendar-card">
                    <div className="calendar-date">{date}</div>
                    <div className="calendar-slots">
                      {ranges.map((range, idx) => (
                        <div key={idx} className="time-slot">
                          <strong>
                            {new Date(range.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - 
                            {new Date(range.endTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                          </strong>
                          <small>Каб. {range.room} | Уч. {range.district}</small>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {}
      {editDoctor && (
        <EditDoctorModal
          doctor={editDoctor}
          specialties={dicts.specialties}
          onClose={() => { setEditDoctor(null); loadDoctors(page) }}
        />
      )}

      {}
      {isCreateModalOpen && (
        <CreateDoctorModal
          specialties={dicts.specialties}
          onClose={() => { setCreateModalOpen(false); loadDoctors(page) }}
        />
      )}
    </div>
  )
}


function EditDoctorModal({ doctor, specialties, onClose }) {
  const initialSpecId = specialties.find(s => s.name === doctor.specialty)?.id || ''
  
  const [form, setForm] = useState({
    firstName: doctor.firstName,
    lastName: doctor.lastName,
    middleName: doctor.middleName || '',
    specialtyId: initialSpecId
  })

  
  const [newSchedules, setNewSchedules] = useState([])
  const [currentSlot, setCurrentSlot] = useState({ from: '', to: '', room: '', district: '' })

  const handleUpdateProfile = async (e) => {
    e.preventDefault()
    try {
      await doctors.update(doctor.id, form) //
      alert('Данные обновлены')
    } catch (e) { alert('Ошибка: ' + (e.response?.data?.message || e.message)) }
  }

  
  const addSlotToList = (e) => {
    e.preventDefault()
    if(!currentSlot.from || !currentSlot.to) return alert("Заполните время")
    if(new Date(currentSlot.from) >= new Date(currentSlot.to)) return alert("Дата окончания должна быть позже начала")
    
    setNewSchedules([...newSchedules, { ...currentSlot, id: Date.now() }])
    setCurrentSlot({ from: '', to: '', room: currentSlot.room, district: currentSlot.district })
  }

  const removeSlotFromList = (id) => {
    setNewSchedules(newSchedules.filter(s => s.id !== id))
  }

  
  const submitSchedules = async () => {
    if(newSchedules.length === 0) return;
    
    
    const schedulesPayload = newSchedules.map(s => ({
      from: toServerDate(s.from),
      to: toServerDate(s.to),
      room: s.room,
      district: s.district
    }))

    try {
      
      await doctors.update(doctor.id, { schedules: schedulesPayload }) 
      alert(`Успешно добавлено смен: ${newSchedules.length}`)
      setNewSchedules([]) 
      onClose() 
    } catch(e) {
      alert('Ошибка при сохранении расписания')
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content large">
        <div className="modal-header">
          <h3>Редактирование: {doctor.lastName}</h3>
          <button className="close-btn" onClick={onClose}>✕</button>
        </div>

        <div className="row" style={{display:'flex', gap:30}}>
          {}
          <div style={{flex:1}}>
            <h4 style={{ marginBottom: 15 }}>Личные данные</h4>
            <form onSubmit={handleUpdateProfile}>
              <div className="form-group">
                 <label>Фамилия</label>
                 <input value={form.lastName} onChange={e => setForm({ ...form, lastName: e.target.value })} required />
              </div>
              <div className="form-group">
                 <label>Имя</label>
                 <input value={form.firstName} onChange={e => setForm({ ...form, firstName: e.target.value })} required />
              </div>
              <div className="form-group">
                 <label>Отчество</label>
                 <input value={form.middleName} onChange={e => setForm({ ...form, middleName: e.target.value })} />
              </div>
              <div className="form-group">
                 <label>Специальность</label>
                 <select value={form.specialtyId} onChange={e => setForm({ ...form, specialtyId: e.target.value })} required>
                  <option value="">Выберите...</option>
                  {specialties.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                </select>
              </div>
              <button className="btn" style={{ marginTop: 15, width: '100%' }}>Сохранить ФИО</button>
            </form>
          </div>

          {}
          <div style={{ flex: 1.5, borderLeft: '1px solid #eee', paddingLeft: 20 }}>
            <h4 style={{ marginBottom: 15 }}> Добавление смен</h4>
            <div style={{ background: '#f9fafb', padding: 15, borderRadius: 8 }}>
              <form onSubmit={addSlotToList}>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10 }}>
                  <div>
                    <label>Начало</label>
                    <input type="datetime-local" value={currentSlot.from} onChange={e => setCurrentSlot({ ...currentSlot, from: e.target.value })} required />
                  </div>
                  <div>
                    <label>Конец</label>
                    <input type="datetime-local" value={currentSlot.to} onChange={e => setCurrentSlot({ ...currentSlot, to: e.target.value })} required />
                  </div>
                  <div>
                    <label>Кабинет</label>
                    <input value={currentSlot.room} onChange={e => setCurrentSlot({ ...currentSlot, room: e.target.value })} required />
                  </div>
                  <div>
                    <label>Участок</label>
                    <input value={currentSlot.district} onChange={e => setCurrentSlot({ ...currentSlot, district: e.target.value })} required />
                  </div>
                </div>
                <button className="btn secondary small" style={{ marginTop: 10, width: '100%' }}>+ Добавить в список</button>
              </form>
            </div>

            {}
            {newSchedules.length > 0 && (
              <div style={{marginTop:15}}>
                <h5>Новые смены (не сохранены):</h5>
                <ul className="dict-list" style={{maxHeight: 200, overflowY:'auto'}}>
                  {newSchedules.map(s => (
                    <li key={s.id}>
                      <small>
                        {new Date(s.from).toLocaleDateString()} {new Date(s.from).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})} - {new Date(s.to).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})}
                        <br/>(Каб: {s.room})
                      </small>
                      <button className="btn small danger" onClick={()=>removeSlotFromList(s.id)}>✕</button>
                    </li>
                  ))}
                </ul>
                <button className="btn success" style={{width:'100%', marginTop:10}} onClick={submitSchedules}>
                  Сохранить {newSchedules.length} смен
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}


function CreateDoctorModal({ specialties, onClose }) {
  const [form, setForm] = useState({ 
    login: '', password: '', firstName: '', lastName: '', middleName: '', specialtyId: '', 
    schedules: [] 
  })



  const submit = async (e) => {
    e.preventDefault()
    try { 
      
      const payload = { ...form, schedules: [] }
      await doctors.create(payload)
      alert('Врач успешно создан!') 
      onClose() 
    } 
    catch(e) { 
      alert('Ошибка: ' + (e.response?.data?.message || JSON.stringify(e.response?.data))) 
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
           <h3>Новый сотрудник</h3>
           <button className="close-btn" onClick={onClose}>✕</button>
        </div>
        <form onSubmit={submit}>
           <div className="form-grid">
             <div><label>Имя *</label><input value={form.firstName} onChange={e=>setForm({...form, firstName:e.target.value})} required/></div>
             <div><label>Фамилия *</label><input value={form.lastName} onChange={e=>setForm({...form, lastName:e.target.value})} required/></div>
           </div>
           <label>Отчество</label><input value={form.middleName} onChange={e=>setForm({...form, middleName:e.target.value})}/>
           
           <label>Специальность *</label>
           <select value={form.specialtyId} onChange={e=>setForm({...form, specialtyId:e.target.value})} required>
             <option value="">Выберите...</option>
             {specialties.map(s=><option key={s.id} value={s.id}>{s.name}</option>)}
           </select>

           <div className="divider">Учетная запись</div>
           <div className="form-grid">
             <div><label>Логин *</label><input value={form.login} onChange={e=>setForm({...form, login:e.target.value})} required/></div>
             <div><label>Пароль *</label><input type="password" value={form.password} onChange={e=>setForm({...form, password:e.target.value})} required/></div>
           </div>

           <div style={{marginTop:20, display:'flex', gap:10}}>
             <button className="btn" style={{flex:1}}>Создать</button>
           </div>
           <small style={{display:'block', marginTop:10, color:'#666', textAlign:'center'}}>График работы можно добавить после создания в режиме редактирования</small>
        </form>
      </div>
    </div>
  )
}


function DictionariesView({ dicts, reload }) {
  const [section, setSection] = useState('specialties')
  const [editingId, setEditingId] = useState(null)
  const [editName, setEditName] = useState('')

  const map = {
    specialties: { title: 'Специальности', data: dicts.specialties },
    medications: { title: 'Лекарства', data: dicts.meds },
    procedures: { title: 'Процедуры', data: dicts.procs },
    tests: { title: 'Анализы', data: dicts.tests },
  }

  
  const handleCreate = async () => {
    const name = prompt('Введите название:')
    if (name) {
      try {
        await dictionaries.create(section, { name })
        reload()
      } catch(e) { alert('Ошибка создания') }
    }
  }

  
  const handleDelete = async (id) => {
    if (window.confirm('Удалить запись?')) {
      try {
        await dictionaries.delete(section, id)
        reload()
      } catch(e) { alert('Ошибка удаления (возможно запись используется)') }
    }
  }

  
  const startEdit = (item) => {
    setEditingId(item.id)
    setEditName(item.name)
  }

  
  const saveEdit = async (id) => {
    try {
      
      await dictionaries.patch(section, { id, name: editName })
      setEditingId(null)
      reload()
    } catch(e) {
      alert('Ошибка обновления')
    }
  }

  return (
    <div>
      <div style={{ display: 'flex', gap: 10, marginBottom: 20 }}>
        {Object.keys(map).map(k => (
          <button key={k} className={`btn small ${section === k ? '' : 'secondary'}`} onClick={() => {setSection(k); setEditingId(null)}}>
            {map[k].title}
          </button>
        ))}
      </div>
      
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom:15 }}>
          <h4>{map[section].title} ({map[section].data.length})</h4>
          <button className="btn small" onClick={handleCreate}>+ Добавить</button>
        </div>
        
        <ul className="dict-list">
          {map[section].data.map(i => (
            <li key={i.id} style={{display:'flex', alignItems:'center', justifyContent:'space-between'}}>
              {editingId === i.id ? (
                <div style={{display:'flex', gap:5, flex:1}}>
                  <input 
                    value={editName} 
                    onChange={e=>setEditName(e.target.value)} 
                    style={{padding:'4px 8px', flex:1}}
                    autoFocus
                  />
                  <button className="btn small success" onClick={()=>saveEdit(i.id)}>OK</button>
                  <button className="btn small secondary" onClick={()=>setEditingId(null)}>✕</button>
                </div>
              ) : (
                <>
                  <span>{i.name}</span>
                  <div style={{display:'flex', gap:5}}>
                    <button className="btn small secondary" onClick={()=>startEdit(i)}>✎</button>
                    <button className="btn small danger" onClick={()=>handleDelete(i.id)}>✕</button>
                  </div>
                </>
              )}
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}