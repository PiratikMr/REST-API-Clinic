import React, { useEffect, useState } from 'react'
import { dictionaries, doctors } from '../api'
import { groupSlotsToRanges, toServerDate } from '../utils/dateHelper'

export default function Admin({ user }) {
  const [tab, setTab] = useState('doctors')
  
  // Doctors State
  const [doctorsList, setDoctorsList] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)

  // Modals State
  const [viewDoctor, setViewDoctor] = useState(null)
  const [editDoctor, setEditDoctor] = useState(null)
  const [isCreateModalOpen, setCreateModalOpen] = useState(false)

  useEffect(() => {
    if (tab === 'doctors') loadDoctors(page)
  }, [tab, page])

  const loadDoctors = async (pageNumber) => {
    setLoading(true)
    try {
      const res = await doctors.list({ page: pageNumber, size: 20 })
      
      
      
      setDoctorsList(res.data.content || []) 
      setTotalPages(res.data.totalPages)
      
    } catch (e) { console.error(e) }
    setLoading(false)
  }

  const handleDetails = async (id, action) => {
    try {
      const res = await doctors.detail(id)
      if (action === 'view') setViewDoctor(res.data)
      if (action === 'edit') setEditDoctor(res.data)
    } catch(e) { alert('Ошибка загрузки') }
  }

  return (
    <div className="panel">
      <div className="admin-header">
        <div>
           <h2>Панель управления</h2>
           <p style={{color: '#6b7280', marginTop: -15}}>Администрирование клиники</p>
        </div>
      </div>
      
      <div className="tabs">
        <button className={`tab-btn ${tab === 'doctors' ? 'active' : ''}`} onClick={() => setTab('doctors')}>Врачи</button>
        <button className={`tab-btn ${tab === 'catalogs' ? 'active' : ''}`} onClick={() => setTab('catalogs')}>Справочники</button>
      </div>

      {tab === 'doctors' && (
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 20, alignItems: 'center' }}>
            <h3>Персонал клиники</h3>
            <button className="btn" onClick={() => setCreateModalOpen(true)}>Добавить врача</button>
          </div>

          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>ФИО Сотрудника</th>
                  <th>Специальность</th>
                  <th style={{ textAlign: 'right' }}>Действия</th>
                </tr>
              </thead>
              <tbody>
                {doctorsList.length === 0 && (
                   <tr><td colSpan="3" style={{textAlign:'center', padding:20, color:'#999'}}>Список врачей пуст</td></tr>
                )}
                {doctorsList.map(d => (
                  <tr key={d.id}>
                    <td><strong>{d.lastName} {d.firstName} {d.middleName}</strong></td>
                    <td><span className="badge-gray">{d.specialty || 'Не указана'}</span></td>
                    <td style={{ textAlign: 'right' }}>
                      <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                        <button className="btn small secondary" onClick={() => handleDetails(d.id, 'view')}>График</button>
                        <button className="btn small secondary" onClick={() => handleDetails(d.id, 'edit')}>Редактировать</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          
          {totalPages > 1 && (
             <div style={{ marginTop: 20, display: 'flex', justifyContent: 'center', gap: 10 }}>
                <button className="btn secondary small" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Назад</button>
                <span style={{display:'flex', alignItems:'center', fontSize:'14px'}}>Страница {page + 1} из {totalPages}</span>
                <button className="btn secondary small" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Вперед</button>
             </div>
          )}
        </div>
      )}

      {tab === 'catalogs' && <DictionariesView />}

      {/* --- MODALS --- */}
      {viewDoctor && (
        <div className="modal-overlay" onClick={() => setViewDoctor(null)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Карточка врача</h3>
              <button className="close-btn" onClick={() => setViewDoctor(null)}>✕</button>
            </div>
            <div>
              <h2 style={{marginBottom: 5}}>{viewDoctor.lastName} {viewDoctor.firstName} {viewDoctor.middleName}</h2>
              <div className="badge-gray" style={{display:'inline-block', marginBottom: 20}}>{viewDoctor.specialty}</div>
              
              <div style={{borderTop: '1px solid #eee', paddingTop: 15}}>
                <h4>Рабочий график</h4>
                {!viewDoctor.schedules?.length ? <p style={{color:'#999'}}>График не установлен</p> : (
                   <ul className="dict-list">
                     {viewDoctor.schedules.map((s, i) => (
                       <li key={i} style={{fontSize:'14px'}}>
                         {new Date(s.startTime).toLocaleDateString()} с {new Date(s.startTime).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})} до {new Date(s.endTime).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})}
                         <span style={{marginLeft: 10, color:'#666'}}>(Каб. {s.room})</span>
                       </li>
                     ))}
                   </ul>
                )}
              </div>
            </div>
          </div>
        </div>
      )}

      {editDoctor && <EditDoctorModal doctor={editDoctor} onClose={() => { setEditDoctor(null); loadDoctors(page) }} />}
      {isCreateModalOpen && <CreateDoctorModal onClose={() => { setCreateModalOpen(false); loadDoctors(page) }} />}
    </div>
  )
}

// --- СПРАВОЧНИКИ ---
function DictionariesView() {
  const [section, setSection] = useState('specialties')
  const [items, setItems] = useState([])
  const [search, setSearch] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editingItem, setEditingItem] = useState(null)

  const map = {
    specialties: { title: 'Специальности' },
    medications: { title: 'Лекарства' },
    procedures: { title: 'Процедуры' },
    tests: { title: 'Анализы' },
  }

  useEffect(() => { loadData() }, [section, search])

  const loadData = async () => {
    try {
      const res = await dictionaries[section]({ search: search, page: 0, size: 100 })
      // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ТОЖЕ ---
      setItems(res.data.data || []) 
    } catch (e) { console.error(e) }
  }

  const handleSave = async (name) => {
    try {
      if (editingItem) await dictionaries.patch(section, { id: editingItem.id, name })
      else await dictionaries.create(section, { name })
      setModalOpen(false)
      loadData()
    } catch (e) { alert('Ошибка сохранения') }
  }

  const handleDelete = async (id) => {
    if (window.confirm('Удалить запись?')) {
        await dictionaries.delete(section, id); loadData();
    }
  }

  return (
    <div className="card">
      <div style={{ display: 'flex', gap: 10, marginBottom: 20, borderBottom: '1px solid #eee', paddingBottom: 15 }}>
        {Object.keys(map).map(k => (
          <button key={k} className={`btn small ${section === k ? '' : 'secondary'}`} onClick={() => { setSection(k); setSearch('') }}>
            {map[k].title}
          </button>
        ))}
      </div>

      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 15 }}>
        <input placeholder="Поиск..." value={search} onChange={e => setSearch(e.target.value)} style={{ maxWidth: 300 }} />
        <button className="btn" onClick={() => { setEditingItem(null); setModalOpen(true) }}>Добавить запись</button>
      </div>

      <ul className="dict-list">
        {items.length === 0 && <li style={{color:'#999', textAlign:'center'}}>Нет данных</li>}
        {items.map(i => (
          <li key={i.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span>{i.name}</span>
            <div style={{ display: 'flex', gap: 5 }}>
              <button className="btn small secondary" onClick={() => { setEditingItem(i); setModalOpen(true) }}>Изм.</button>
              <button className="btn small danger" onClick={() => handleDelete(i.id)}>Удалить</button>
            </div>
          </li>
        ))}
      </ul>

      {modalOpen && (
        <div className="modal-overlay" onClick={() => setModalOpen(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{editingItem ? 'Редактировать' : 'Новая запись'}</h3>
              <button className="close-btn" onClick={() => setModalOpen(false)}>✕</button>
            </div>
            <form onSubmit={(e) => { e.preventDefault(); handleSave(e.target.elements.name.value) }}>
              <label>Название</label>
              <input name="name" defaultValue={editingItem?.name || ''} autoFocus required />
              <div style={{ marginTop: 20, display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
                <button type="button" className="btn secondary" onClick={() => setModalOpen(false)}>Отмена</button>
                <button className="btn">Сохранить</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

// --- Create/Edit Doctor Modals ---

function CreateDoctorModal({ onClose }) {
  const [specialties, setSpecialties] = useState([])
  const [form, setForm] = useState({ login: '', password: '', firstName: '', lastName: '', middleName: '', specialtyId: '' })

  useEffect(() => { 
      dictionaries.specialties({size:100}).then(res => setSpecialties(res.data.content || [])) // Используем content!
  }, [])

  const submit = async (e) => {
    e.preventDefault()
    if (!form.specialtyId) return alert('Выберите специальность')
    
    try { 
        
        await doctors.create({ ...form, specialtyId: Number(form.specialtyId), schedules: [] }); 
        onClose(); 
    } 
    catch(e) { alert('Ошибка создания: ' + (e.response?.data?.message || 'Error')) }
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header"><h3>Новый сотрудник</h3><button className="close-btn" onClick={onClose}>✕</button></div>
        <form onSubmit={submit}>
           <div className="form-grid">
             <div><label>Фамилия</label><input value={form.lastName} onChange={e=>setForm({...form, lastName:e.target.value})} required/></div>
             <div><label>Имя</label><input value={form.firstName} onChange={e=>setForm({...form, firstName:e.target.value})} required/></div>
           </div>
           <label>Отчество</label><input value={form.middleName} onChange={e=>setForm({...form, middleName:e.target.value})}/>
           <label>Специальность</label>
           <select value={form.specialtyId} onChange={e=>setForm({...form, specialtyId:e.target.value})} required>
             <option value="">Выберите...</option>
             {specialties.map(s=><option key={s.id} value={s.id}>{s.name}</option>)}
           </select>
           <h4 style={{marginTop:20, marginBottom:10}}>Данные для входа</h4>
           <div className="form-grid">
             <div><label>Логин</label><input value={form.login} onChange={e=>setForm({...form, login:e.target.value})} required/></div>
             <div><label>Пароль</label><input type="password" value={form.password} onChange={e=>setForm({...form, password:e.target.value})} required/></div>
           </div>
           <button className="btn" style={{marginTop:20, width:'100%'}}>Создать</button>
        </form>
      </div>
    </div>
  )
}

function EditDoctorModal({ doctor, onClose }) {
  const [specialties, setSpecialties] = useState([])
  useEffect(() => { 
      dictionaries.specialties({size:100}).then(res => setSpecialties(res.data.content || [])) // Используем content!
  }, [])
  
  const [form, setForm] = useState({
    firstName: doctor.firstName, lastName: doctor.lastName, middleName: doctor.middleName || '',
    specialtyId: ''
  })
  
  useEffect(() => {
     if(specialties.length) {
         const s = specialties.find(x => x.name === doctor.specialty);
         if(s) setForm(f => ({...f, specialtyId: s.id}))
     }
  }, [specialties, doctor])

  const [newSchedules, setNewSchedules] = useState([])
  const [slot, setSlot] = useState({ from: '', to: '', room: '', district: '' })

  const updateProfile = async (e) => {
    e.preventDefault(); 
    await doctors.update(doctor.id, form); 
    alert('Сохранено');
  }

  const addSchedule = async () => {
    if(!newSchedules.length) return;
    const payload = newSchedules.map(s => ({ from: toServerDate(s.from), to: toServerDate(s.to), room: s.room, district: s.district }))
    await doctors.update(doctor.id, { schedules: payload })
    onClose()
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content large">
        <div className="modal-header"><h3>Редактирование сотрудника</h3><button className="close-btn" onClick={onClose}>✕</button></div>
        <div style={{display:'flex', gap:30}}>
           <div style={{flex:1}}>
              <h4>Личные данные</h4>
              <form onSubmit={updateProfile}>
                 <label>Фамилия</label><input value={form.lastName} onChange={e=>setForm({...form, lastName:e.target.value})} />
                 <label>Имя</label><input value={form.firstName} onChange={e=>setForm({...form, firstName:e.target.value})} />
                 <label>Специальность</label>
                 <select value={form.specialtyId} onChange={e=>setForm({...form, specialtyId:e.target.value})}>
                    {specialties.map(s=><option key={s.id} value={s.id}>{s.name}</option>)}
                 </select>
                 <button className="btn secondary" style={{marginTop:10, width:'100%'}}>Обновить ФИО</button>
              </form>
           </div>
           <div style={{flex:1.5, paddingLeft:20, borderLeft:'1px solid #eee'}}>
              <h4>Добавление смен</h4>
              <div style={{background:'#f9fafb', padding:15, borderRadius:8}}>
                  <div className="form-grid">
                      <div><label>C</label><input type="datetime-local" value={slot.from} onChange={e=>setSlot({...slot, from:e.target.value})}/></div>
                      <div><label>По</label><input type="datetime-local" value={slot.to} onChange={e=>setSlot({...slot, to:e.target.value})}/></div>
                  </div>
                  <div className="form-grid">
                      <div><label>Кабинет</label><input value={slot.room} onChange={e=>setSlot({...slot, room:e.target.value})}/></div>
                      <div><label>Участок</label><input value={slot.district} onChange={e=>setSlot({...slot, district:e.target.value})}/></div>
                  </div>
                  <button className="btn secondary small" style={{marginTop:10, width:'100%'}} 
                    onClick={()=>{setNewSchedules([...newSchedules, {...slot, id:Date.now()}]); setSlot({...slot, from:'', to:''})}}>
                    Добавить в список
                  </button>
              </div>
              {newSchedules.length > 0 && (
                 <div style={{marginTop:10}}>
                    <ul className="dict-list">
                       {newSchedules.map(s => <li key={s.id}>{s.from} - {s.to} (Каб. {s.room})</li>)}
                    </ul>
                    <button className="btn success" style={{width:'100%', marginTop:10}} onClick={addSchedule}>Сохранить смены</button>
                 </div>
              )}
           </div>
        </div>
      </div>
    </div>
  )
}