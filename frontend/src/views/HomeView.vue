<template>
  <div class="container">
    <h2>Quản lý Người dùng (CRUD)</h2>

    <!-- Form Thêm / Cập nhật -->
    <div class="form-box">
      <h3>{{ isEditing ? 'Cập nhật User' : 'Thêm User mới' }}</h3>
      <form @submit.prevent="saveUser">
        <input v-model="form.name" placeholder="Tên" required />
        <input v-model="form.email" type="email" placeholder="Email" required />
        <!-- THÊM Ô NHẬP TUỔI -->
        <input v-model.number="form.age" type="number" placeholder="Tuổi" required />

        <button type="submit">{{ isEditing ? 'Cập nhật' : 'Thêm' }}</button>
        <button type="button" v-if="isEditing" @click="resetForm">Hủy</button>
      </form>
    </div>

    <!-- Danh sách User -->
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên</th>
          <th>Email</th>
          <th>Tuổi</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.age }}</td>
          <td>
            <button @click="editUser(user)">Sửa</button>
            <button @click="deleteUser(user.id)" class="btn-delete">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// const API_URL = 'http://localhost:8080/api/users'
const API_URL = '/api/users'

const users = ref([])
const isEditing = ref(false)
const form = ref({
  id: null,
  name: '',
  email: '',
  age: null // THÊM TRƯỜNG AGE
})

const fetchUsers = async () => {
  try {
    const res = await fetch(API_URL)
    if (res.ok) users.value = await res.json()
  } catch (err) {
    console.error('Lỗi khi tải dữ liệu:', err)
  }
}

const saveUser = async () => {
  try {
    const method = isEditing.value ? 'PUT' : 'POST'
    const url = isEditing.value ? `${API_URL}/${form.value.id}` : API_URL

    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: form.value.name,
        email: form.value.email,
        age: form.value.age // GỬI THÊM AGE VỀ BACKEND
      })
    })

    if (res.ok) {
      await fetchUsers()
      resetForm()
    } else {
      alert('Có lỗi xảy ra từ máy chủ!')
    }
  } catch (err) {
    console.error('Lỗi khi lưu dữ liệu:', err)
  }
}

const deleteUser = async (id) => {
  if (!confirm('Bạn có chắc muốn xóa không?')) return
  try {
    const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' })
    if (res.ok) fetchUsers()
  } catch (err) {
    console.error('Lỗi khi xóa:', err)
  }
}

const editUser = (user) => {
  isEditing.value = true
  form.value = { ...user }
}

const resetForm = () => {
  isEditing.value = false
  form.value = { id: null, name: '', email: '', age: null }
}

onMounted(() => {
  fetchUsers()
})
</script>