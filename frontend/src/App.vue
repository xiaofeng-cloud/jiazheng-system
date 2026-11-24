<script setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { useAuthStore } from './stores/auth'
import { ArrowRight, CircleCheck, DataBoard, Search, Tickets, TrendCharts, User } from '@element-plus/icons-vue'
import http from './api/http'

const auth = useAuthStore()
const loginForm = ref({ username: 'admin', password: '123456' })
const loginError = ref('')
const activeMenu = ref('dashboard')
const orderFilter = ref('全部')
const chartRef = ref(null)
const dashboard = ref({ orderStatus: [], orderTrend: [], averageScore: 0, completedCount: 0, pendingComplaintCount: 0 })
const orders = ref([])
const filteredOrders = computed(() => orderFilter.value === '全部' ? orders.value : orders.value.filter((item) => item.status === orderFilter.value))
const statusType = { 待审核: 'warning', 待分配: 'warning', 服务中: 'primary', 待确认: 'danger', 已完成: 'success' }
const menuItems = [{ key: 'dashboard', label: '工作台', icon: 'DataBoard' }, { key: 'orders', label: '工单管理', icon: 'Tickets' }, { key: 'providers', label: '服务人员', icon: 'User' }, { key: 'quality', label: '质量监督', icon: 'CircleCheck' }, { key: 'statistics', label: '统计分析', icon: 'TrendCharts' }]
const metrics = computed(() => {
  const statusCount = (name) => dashboard.value.orderStatus.find((item) => item.label === name)?.value || 0
  return [{ label: '已完成工单', value: dashboard.value.completedCount, trend: '累计', color: '#2f80ed' }, { label: '待分配', value: statusCount('待分配'), trend: '需关注', color: '#f2994a' }, { label: '服务中', value: statusCount('服务中'), trend: '当前', color: '#27ae60' }, { label: '平均评分', value: Number(dashboard.value.averageScore).toFixed(2), trend: '全部评价', color: '#9b51e0' }]
})

async function enterSystem() {
  loginError.value = ''
  try { await auth.login(loginForm.value) } catch (error) { loginError.value = error.response?.data?.message || '登录失败，请检查账号和密码'; return }
  await loadDashboard()
  await loadOrders()
  await nextTick()
  renderChart()
}
async function loadOrders() {
  try {
    const { data } = await http.get('/work-orders')
    const rows = data.data || data || []
    orders.value = rows.map((row) => ({ no: row.orderNo, item: `服务项目 #${row.serviceItemId}`, customer: `客户 #${row.customerId}`, provider: row.providerId ? `服务员 #${row.providerId}` : '未分配', status: row.status, date: row.scheduledDate, region: '服务区域待补充' }))
  } catch { orders.value = [] }
}
async function loadDashboard() {
  try { const { data } = await http.get('/statistics/dashboard'); dashboard.value = data.data || data } catch { dashboard.value = { orderStatus: [], orderTrend: [], averageScore: 0, completedCount: 0, pendingComplaintCount: 0 } }
}
function renderChart() {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)
  chart.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 36, right: 18, top: 20, bottom: 24 }, xAxis: { type: 'category', data: dashboard.value.orderTrend.map((item) => item.label) }, yAxis: { type: 'value' }, series: [{ name: '工单数量', type: 'line', smooth: true, data: dashboard.value.orderTrend.map((item) => item.value), itemStyle: { color: '#2f80ed' }, areaStyle: { color: 'rgba(47,128,237,.12)' } }] })
}
function selectMenu(key) { activeMenu.value = key; if (key === 'dashboard') nextTick(renderChart); if (key === 'orders') loadOrders() }
onMounted(async () => { if (auth.loggedIn) { await loadDashboard(); await nextTick(); renderChart() } })
</script>

<template>
  <div v-if="!auth.loggedIn" class="login-page">
    <div class="login-intro"><div class="brand-mark">家</div><h1>家政服务工单管理系统</h1><p>工单调度 · 服务过程 · 质量追踪</p><span>面向客户、家政服务人员和平台管理员的协同工作台</span></div>
    <el-card class="login-card" shadow="never"><h2>登录系统</h2><p class="muted">请输入演示账号进入工作台</p><el-alert v-if="loginError" :title="loginError" type="error" :closable="false" show-icon /><el-form :model="loginForm" @submit.prevent="enterSystem"><el-form-item label="账号"><el-input v-model="loginForm.username" /></el-form-item><el-form-item label="密码"><el-input v-model="loginForm.password" type="password" show-password /></el-form-item><el-button type="primary" class="login-button" @click="enterSystem">登录</el-button></el-form><div class="demo-account">演示账号：admin / customer01 / provider01，密码：123456</div></el-card>
  </div>
  <el-container v-else class="app-shell">
    <el-aside width="220px" class="side-bar"><div class="side-brand"><span class="brand-mark small">家</span><span>家政服务平台</span></div><el-menu :default-active="activeMenu" class="side-menu" @select="selectMenu"><el-menu-item v-for="item in menuItems" :key="item.key" :index="item.key"><el-icon><component :is="item.icon" /></el-icon><span>{{ item.label }}</span></el-menu-item></el-menu><div class="side-foot">纯 Web 管理端<br>SpringBoot + Vue3</div></el-aside>
    <el-container><el-header class="top-bar"><div><span class="page-title">{{ menuItems.find((item) => item.key === activeMenu)?.label }}</span><span class="breadcrumb">/ 服务质量追踪管理</span></div><div class="user-box"><el-tag size="small" effect="plain">{{ auth.roleCode }}</el-tag><span>{{ auth.username }}</span><el-button link @click="auth.logout">退出</el-button></div></el-header><el-main class="content-area">
      <template v-if="activeMenu === 'dashboard'"><div class="welcome-row"><div><h2>工作台</h2><p>统计数据来自后端数据库</p></div><el-button type="primary" @click="activeMenu = 'orders'">查看工单</el-button></div><div class="metric-grid"><el-card v-for="metric in metrics" :key="metric.label" class="metric-card" shadow="never"><div class="metric-label">{{ metric.label }}</div><div class="metric-value" :style="{ color: metric.color }">{{ metric.value }}</div><div class="metric-trend">{{ metric.trend }}</div></el-card></div><div class="dashboard-grid"><el-card shadow="never" class="chart-card"><template #header><div class="card-title">近七日工单趋势 <span>数据库统计</span></div></template><div ref="chartRef" class="chart"></div><el-empty v-if="!dashboard.orderTrend.length" description="暂无统计数据" :image-size="70" /></el-card><el-card shadow="never" class="notice-card"><template #header><div class="card-title">待处理事项</div></template><div class="notice-item"><el-tag type="warning" size="small">待分配工单</el-tag><span>{{ dashboard.orderStatus.find((item) => item.label === '待分配')?.value || 0 }} 条工单等待调度</span><el-icon><ArrowRight /></el-icon></div><div class="notice-item"><el-tag type="danger" size="small">待客户确认</el-tag><span>{{ dashboard.orderStatus.find((item) => item.label === '待确认')?.value || 0 }} 条服务等待验收</span><el-icon><ArrowRight /></el-icon></div><div class="notice-item"><el-tag type="primary" size="small">待处理投诉</el-tag><span>{{ dashboard.pendingComplaintCount }} 条投诉等待处理</span><el-icon><ArrowRight /></el-icon></div></el-card></div></template>
      <template v-else-if="activeMenu === 'orders'"><div class="welcome-row"><div><h2>工单管理</h2><p>查看需求、调度和服务状态</p></div><el-button type="primary">新建服务需求</el-button></div><el-card shadow="never"><div class="filter-row"><el-radio-group v-model="orderFilter" size="small"><el-radio-button v-for="status in ['全部','待审核','待分配','服务中','待确认','已完成']" :key="status" :label="status" /></el-radio-group><el-input placeholder="搜索工单编号或客户" class="search-input" clearable><template #prefix><el-icon><Search /></el-icon></template></el-input></div><el-table :data="filteredOrders" stripe><el-table-column prop="no" label="工单编号" width="150" /><el-table-column prop="item" label="服务项目" /><el-table-column prop="customer" label="客户" width="100" /><el-table-column prop="provider" label="服务人员" width="110" /><el-table-column prop="region" label="服务区域" /><el-table-column prop="date" label="预约日期" width="120" /><el-table-column label="状态" width="110"><template #default="scope"><el-tag :type="statusType[scope.row.status] || 'info'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column><el-table-column label="操作" width="90"><template #default><el-button link type="primary">详情</el-button></template></el-table-column></el-table></el-card></template>
      <el-empty v-else :description="`${menuItems.find((item) => item.key === activeMenu)?.label}模块正在接入`" />
    </el-main></el-container>
  </el-container>
</template>
