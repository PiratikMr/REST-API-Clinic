import axios from 'axios'

const API = axios.create({
  baseURL: 'http://localhost:8090',
  headers: {
    'Content-Type': 'application/json'
  }
})


API.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token.startsWith('Bearer ') ? token : `Bearer ${token}`
  }
  return config
}, err => Promise.reject(err))


API.interceptors.response.use(
  r => r,
  e => {
    if (e.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(e)
  }
)

export const auth = {
  login: (data) => API.post('/auth/login', data),
  register: (data) => API.post('/auth/register', data)
}

export const doctors = {
  list: (params) => API.get('/doctors', { params }),
  create: (data) => API.post('/doctors', data),
  detail: (id) => API.get(`/doctors/${id}`),
  update: (id, data) => API.patch(`/doctors/${id}`, data)
}

export const dictionaries = {
  tests: () => API.get('/dictionaries/tests'),
  procedures: () => API.get('/dictionaries/procedures'),
  medications: () => API.get('/dictionaries/medications'),
  specialties: () => API.get('/dictionaries/specialties'),
  create: (path, body) => API.post(`/dictionaries/${path}`, body),
  patch: (path, body) => API.patch(`/dictionaries/${path}`, body),
  delete: (path, id) => API.delete(`/dictionaries/${path}/${id}`)
}

export const appointments = {
  create: (data) => API.post('/appointments', data),
  getForPatient: (patientId, params) => API.get(`/appointments/patients/${patientId}`, { params }),
  getForDoctor: (doctorId, params) => API.get(`/appointments/doctors/${doctorId}`, { params }),
  detail: (id) => API.get(`/appointments/${id}`),
  close: (id, body) => API.post(`/appointments/${id}/close`, body),
  cancel: (id) => API.post(`/appointments/${id}/cancel`)
}

export const patients = {
  get: (id) => API.get(`/patients/${id}`),
  update: (id, body) => API.patch(`/patients/${id}`, body)
}

export default API