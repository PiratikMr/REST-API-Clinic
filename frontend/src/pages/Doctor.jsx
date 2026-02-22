import React, { useEffect, useState } from 'react'
import { appointments, dictionaries } from '../api'

export default function Doctor({ user }){
  const [appts, setAppts] = useState([])
  const [loading, setLoading] = useState(false)
  const [activeAppt, setActiveAppt] = useState(null)
  
  
  const [dicts, setDicts] = useState({ meds:[], tests:[], procs:[] })

  
  const [newMed, setNewMed] = useState({ id: '', details: '' })
  const [newTest, setNewTest] = useState({ id: '', results: '' })
  const [newProc, setNewProc] = useState({ id: '', sessions: '' })

  useEffect(() => {
    load()
    loadDictionaries()
  }, [user])

  async function load(){
    setLoading(true)
    const docId = user.id || user.doctorId
    try {
      const res = await appointments.getForDoctor(docId, { statuses: "ACTIVE,CLOSED" })
      setAppts(res.data.content || [])
    } catch(e) { console.error(e) }
    setLoading(false)
  }

  async function loadDictionaries() {
    try {
      const [m, t, p] = await Promise.all([
        dictionaries.medications(),
        dictionaries.tests(),
        dictionaries.procedures()
      ])
      setDicts({ meds: m.data, tests: t.data, procs: p.data })
    } catch(e) { console.error(e) }
  }

 
  const [closeForm, setCloseForm] = useState({
    diagnosis: '', complaints: '',
    medications: [],
    tests: [],
    procedures: []
  })

  const openCloseModal = (appt) => {
    setActiveAppt(appt)
    setCloseForm({ diagnosis: '', complaints: '', medications: [], tests: [], procedures: [] })
    // Сброс временных полей
    setNewMed({ id: '', details: '' })
    setNewTest({ id: '', results: '' })
    setNewProc({ id: '', sessions: '' })
  }

  const handleCloseSubmit = async (e) => {
    e.preventDefault()
    if (!closeForm.diagnosis || !closeForm.complaints) return alert('Заполните диагноз и жалобы')
    
    try {
      await appointments.close(activeAppt.id, closeForm)
      alert('Прием успешно закрыт!')
      setActiveAppt(null)
      load()
    } catch(e) {
      alert('Ошибка: ' + (e.response?.data?.message || e.message))
    }
  }

  
  const addItem = (field, item) => setCloseForm(prev => ({...prev, [field]: [...prev[field], item]}))
  const removeItem = (field, idx) => setCloseForm(prev => ({...prev, [field]: prev[field].filter((_, i) => i !== idx)}))

  const formatDateTime = (dt) => new Date(dt).toLocaleString('ru-RU', {day:'numeric', month:'long', hour:'2-digit', minute:'2-digit'})

  return (
    <div className="panel">
      <div className="admin-header">
        <h2>👨‍⚕️ Рабочий стол врача</h2>
        <button className="btn secondary" onClick={load}>↻ Обновить</button>
      </div>

      {loading && <div className="loading-bar">Загрузка данных...</div>}

      <div className="row" style={{ flexDirection:'column', gap: 15 }}>
        {!loading && appts.length === 0 && <div className="empty-state">Нет активных приемов на сегодня</div>}
        
        {appts.map(a => (
          <div key={a.id} className="card" style={{ borderLeft: a.status === 'ACTIVE' ? '4px solid #4338ca' : '4px solid #10b981' }}>
            <div style={{display:'flex', justifyContent:'space-between', alignItems:'flex-start'}}>
              <div>
                 <div style={{display:'flex', gap:12, alignItems:'center', marginBottom:8}}>
                   <span className={`badge-gray`}>{a.status === 'ACTIVE' ? 'Ожидает приема' : 'Завершен'}</span>
                   <strong style={{fontSize:'1.1rem'}}>🕒 {formatDateTime(a.visitInfo.time)}</strong>
                 </div>
                 <div style={{fontSize:'1.2rem', fontWeight:600, marginBottom:5}}>
                   👤 {a.patient.lastName} {a.patient.firstName} {a.patient.middleName}
                 </div>
                 <div style={{color:'#666'}}>
                    Жалобы (предварительно): {a.complaints || '—'}
                 </div>
              </div>
              
              <div>
                {a.status === 'ACTIVE' && (
                  <button className="btn" onClick={() => openCloseModal(a)}> Начать прием</button>
                )}
                {a.status === 'CLOSED' && (
                  <div style={{textAlign:'right', color:'#059669', fontWeight:500}}>
                    ✅ Прием закрыт<br/>
                    <small style={{color:'#666'}}>Дз: {a.diagnosis}</small>
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {}
      {activeAppt && (
        <div className="modal-overlay">
          <div className="modal-content large" style={{maxWidth:'900px'}}>
            <div className="modal-header">
              <h3>Закрытие истории болезни</h3>
              <button className="close-btn" onClick={() => setActiveAppt(null)}>✕</button>
            </div>
            
            <div style={{marginBottom:20, paddingBottom:15, borderBottom:'1px solid #eee'}}>
              <strong>Пациент: </strong> {activeAppt.patient.lastName} {activeAppt.patient.firstName}
            </div>

            <form onSubmit={handleCloseSubmit}>
              <div className="form-grid">
                <div style={{gridColumn: '1 / -1'}}>
                  <label>Жалобы пациента *</label>
                  <textarea rows="2" value={closeForm.complaints} onChange={e=>setCloseForm({...closeForm, complaints:e.target.value})} required />
                </div>
                <div style={{gridColumn: '1 / -1'}}>
                  <label>Поставленный диагноз *</label>
                  <input value={closeForm.diagnosis} onChange={e=>setCloseForm({...closeForm, diagnosis:e.target.value})} required placeholder="Код МКБ или название" />
                </div>
              </div>

              {/* 1. ЛЕКАРСТВА */}
              <div className="section-box" style={{marginTop:20, padding:15, background:'#f8fafc', borderRadius:8, border:'1px solid #e2e8f0'}}>
                <h4 style={{marginBottom:10, color:'#334155'}}> Лекарства</h4>
                {/* Список добавленных */}
                {closeForm.medications.length > 0 && (
                  <ul className="dict-list" style={{marginBottom:15, background:'white', borderRadius:6, border:'1px solid #eee'}}>
                    {closeForm.medications.map((m, i) => (
                      <li key={i} style={{padding:'8px 12px'}}>
                        <span style={{fontWeight:500}}>{dicts.meds.find(d=>d.id == m.medicationId)?.name}</span>
                        <span style={{color:'#666', fontSize:'0.9rem', margin:'0 10px'}}>— {m.details}</span>
                        <button type="button" className="btn small danger" onClick={()=>removeItem('medications', i)}>✕</button>
                      </li>
                    ))}
                  </ul>
                )}
                {}
                <div style={{display:'grid', gridTemplateColumns:'1fr 1fr auto', gap:10}}>
                  <select value={newMed.id} onChange={e=>setNewMed({...newMed, id:e.target.value})}>
                    <option value="">Выберите препарат...</option>
                    {dicts.meds.map(d=><option key={d.id} value={d.id}>{d.name}</option>)}
                  </select>
                  <input placeholder="Дозировка (напр. 2 раза в день)" value={newMed.details} onChange={e=>setNewMed({...newMed, details:e.target.value})} />
                  <button type="button" className="btn secondary small" disabled={!newMed.id} onClick={()=>{
                    addItem('medications', { medicationId: newMed.id, details: newMed.details })
                    setNewMed({ id: '', details: '' })
                  }}>+ Добавить</button>
                </div>
              </div>

              {/* 2. АНАЛИЗЫ */}
              <div className="section-box" style={{marginTop:15, padding:15, background:'#f8fafc', borderRadius:8, border:'1px solid #e2e8f0'}}>
                <h4 style={{marginBottom:10, color:'#334155'}}> Анализы</h4>
                {closeForm.tests.length > 0 && (
                  <ul className="dict-list" style={{marginBottom:15, background:'white', borderRadius:6, border:'1px solid #eee'}}>
                     {closeForm.tests.map((t, i) => (
                      <li key={i} style={{padding:'8px 12px'}}>
                        <span style={{fontWeight:500}}>{dicts.tests.find(d=>d.id == t.testId)?.name}</span>
                        <span style={{color:'#666', fontSize:'0.9rem', margin:'0 10px'}}>Result: {t.results}</span>
                        <button type="button" className="btn small danger" onClick={()=>removeItem('tests', i)}>✕</button>
                      </li>
                    ))}
                  </ul>
                )}
                <div style={{display:'grid', gridTemplateColumns:'1fr 1fr auto', gap:10}}>
                  <select value={newTest.id} onChange={e=>setNewTest({...newTest, id:e.target.value})}>
                    <option value="">Выберите анализ...</option>
                    {dicts.tests.map(d=><option key={d.id} value={d.id}>{d.name}</option>)}
                  </select>
                  <input placeholder="Результат (если есть)" value={newTest.results} onChange={e=>setNewTest({...newTest, results:e.target.value})} />
                  <button type="button" className="btn secondary small" disabled={!newTest.id} onClick={()=>{
                    addItem('tests', { testId: newTest.id, results: newTest.results || 'Назначено' })
                    setNewTest({ id: '', results: '' })
                  }}>+ Добавить</button>
                </div>
              </div>

              {}
              <div className="section-box" style={{marginTop:15, padding:15, background:'#f8fafc', borderRadius:8, border:'1px solid #e2e8f0'}}>
                <h4 style={{marginBottom:10, color:'#334155'}}> Процедуры</h4>
                {closeForm.procedures.length > 0 && (
                   <ul className="dict-list" style={{marginBottom:15, background:'white', borderRadius:6, border:'1px solid #eee'}}>
                    {closeForm.procedures.map((p, i) => (
                      <li key={i} style={{padding:'8px 12px'}}>
                        <span style={{fontWeight:500}}>{dicts.procs.find(d=>d.id == p.procedureId)?.name}</span>
                        <span style={{color:'#666', fontSize:'0.9rem', margin:'0 10px'}}>Сеансов: {p.sessions}</span>
                        <button type="button" className="btn small danger" onClick={()=>removeItem('procedures', i)}>✕</button>
                      </li>
                    ))}
                  </ul>
                )}
                <div style={{display:'grid', gridTemplateColumns:'1fr 100px auto', gap:10}}>
                  <select value={newProc.id} onChange={e=>setNewProc({...newProc, id:e.target.value})}>
                    <option value="">Выберите процедуру...</option>
                    {dicts.procs.map(d=><option key={d.id} value={d.id}>{d.name}</option>)}
                  </select>
                  <input type="number" min="1" placeholder="Кол-во" value={newProc.sessions} onChange={e=>setNewProc({...newProc, sessions:e.target.value})} />
                  <button type="button" className="btn secondary small" disabled={!newProc.id || !newProc.sessions} onClick={()=>{
                    addItem('procedures', { procedureId: newProc.id, sessions: parseInt(newProc.sessions) })
                    setNewProc({ id: '', sessions: '' })
                  }}>+ Добавить</button>
                </div>
              </div>

              <div style={{display:'flex', gap:15, marginTop:30, paddingTop:20, borderTop:'1px solid #eee'}}>
                <button className="btn" style={{flex:1, padding:12, fontSize:'1rem'}}>Завершить и сохранить</button>
                <button type="button" className="btn secondary" onClick={()=>setActiveAppt(null)}>Отмена</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}