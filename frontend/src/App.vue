<script setup>
import { computed, nextTick, ref } from 'vue'
import * as echarts from 'echarts'
import { useAuthStore } from './stores/auth'
import { ArrowRight, CircleCheck, DataBoard, Search, Tickets, TrendCharts, User } from '@element-plus/icons-vue'

const auth = useAuthStore()
const loginForm = ref({ username: 'admin', password: '123456' })
const activeMenu = ref('dashboard')
const orderFilter = ref('全部')
const chartRef = ref(null)
const orders = ref([
  { no: 'WO20260001', item: '全屋日常保洁', customer: '客户1', provider: '服务员1', status: '待审核', date: '2026-01-06', region: '北京市东城区' },
  { no: 'WO20260002', item: '空调清洗', customer: '客户2', provider: '服务员2', status: '服务中', date: '2026-01-07', region: '北京市朝阳区' },
  { no: 'WO20260003', item: '老人陪护', customer: '客户3', provider: '服务员3', status: '待确认', date: '2026-01-07', region: '上海市浦东新区' },
  { no: 'WO20260004', item: '衣物收纳', customer: '客户4', provider: '服务员4', status: '已完成', date: '2026-01-05', region: '广州市天河区' },
  { no: 'WO20260005', item: '深度保洁', customer: '客户5', provider: '未分配', status: '待分配', date: '2026-01-08', region: '成都市武侯区' },
])
const filteredOrders = computed(() => orderFilter.value === '全部' ? orders.value : orders.value.filter((item) => item.status === orderFilter.value))
const statusType = { 待审核: 'warning', 待分配: 'warning', 服务中: 'primary', 待确认: 'danger', 已完成: 'success' }
const menuItems = [{ key: 'dashboard', label: '工作台', icon: 'DataBoard' }, { key: 'orders', label: '工单管理', icon: 'Tickets' }, { key: 'providers', label: '服务人员', icon: 'User' }, { key: 'quality', label: '质量监督', icon: 'CircleCheck' }, { key: 'statistics', label: '统计分析', icon: 'TrendCharts' }]

async function enterSystem() {
  await auth.login(loginForm.value)
  await nextTick()
  renderChart()
}
function renderChart() {
  if (!chartRef.value) return
  const chart = echarts.init(chartRef.value)
  chart.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 36, right: 18, top: 20, bottom: 24 }, xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] }, yAxis: { type: 'value' }, series: [{ name: '完成工单', type: 'line', smooth: true, data: [18, 23, 19, 31, 28, 36, 42], itemStyle: { color: '#2f80ed' }, areaStyle: { color: 'rgba(47,128,237,.12)' } }] })
}
function selectMenu(key) { activeMenu.value = key; if (key === 'dashboard') nextTick(renderChart) }
</script>

<template>
  <div v-if="!auth.loggedIn" class="login-page">
    <div class="login-intro"><div class="brand-mark">家</div><h1>家政服务工单管理系统</h1><p>工单调度 · 服务过程 · 质量追踪</p><span>面向客户、家政服务人员和平台管理员的协同工作台</span></div>
    <el-card class="login-card" shadow="never"><h2>登录系统</h2><p class="muted">请输入演示账号进入工作台</p><el-form :model="loginForm" @submit.prevent="enterSystem"><el-form-item label="账号"><el-input v-model="loginForm.username" /></el-form-item><el-form-item label="密码"><el-input v-model="loginForm.password" type="password" show-password /></el-form-item><el-button type="primary" class="login-button" @click="enterSystem">登录</el-button></el-form><div class="demo-account">演示账号：admin / customer01 / provider01，密码：123456</div></el-card>
  </div>
  <el-container v-else class="app-shell">
    <el-aside width="220px" class="side-bar"><div class="side-brand"><span class="brand-mark small">家</span><span>家政服务平台</span></div><el-menu :default-active="activeMenu" class="side-menu" @select="selectMenu"><el-menu-item v-for="item in menuItems" :key="item.key" :index="item.key"><el-icon><component :is="item.icon" /></el-icon><span>{{ item.label }}</span></el-menu-item></el-menu><div class="side-foot">纯 Web 管理端<br>SpringBoot + Vue3</div></el-aside>
    <el-container><el-header class="top-bar"><div><span class="page-title">{{ menuItems.find((item) => item.key === activeMenu)?.label }}</span><span class="breadcrumb">/ 服务质量追踪管理</span></div><div class="user-box"><el-tag size="small" effect="plain">{{ auth.roleCode }}</el-tag><span>{{ auth.username }}</span><el-button link @click="auth.logout">退出</el-button></div></el-header><el-main class="content-area">
      <template v-if="activeMenu === 'dashboard'"><div class="welcome-row"><div><h2>工作台</h2><p>今天也要把每一张工单服务好</p></div><el-button type="primary" @click="activeMenu = 'orders'">查看工单</el-button></div><div class="metric-grid"><el-card v-for="metric in [{label:'今日工单',value:'28',trend:'+12%',color:'#2f80ed'},{label:'待分配',value:'06',trend:'需关注',color:'#f2994a'},{label:'服务中',value:'15',trend:'+3',color:'#27ae60'},{label:'平均评分',value:'4.86',trend:'+0.12',color:'#9b51e0'}]" :key="metric.label" class="metric-card" shadow="never"><div class="metric-label">{{ metric.label }}</div><div class="metric-value" :style="{ color: metric.color }">{{ metric.value }}</div><div class="metric-trend">{{ metric.trend }} <span>较昨日</span></div></el-card></div><div class="dashboard-grid"><el-card shadow="never" class="chart-card"><template #header><div class="card-title">近七日工单完成趋势 <span>实时数据</span></div></template><div ref="chartRef" class="chart"></div></el-card><el-card shadow="never" class="notice-card"><template #header><div class="card-title">待处理事项 <el-badge :value="6" /></div></template><div v-for="notice in [{title:'待分配工单',desc:'有 6 条工单等待调度',type:'warning'},{title:'待客户确认',desc:'有 3 条服务等待验收',type:'danger'},{title:'质量回访',desc:'本周需回访 8 位客户',type:'primary'}]" :key="notice.title" class="notice-item"><el-tag :type="notice.type" size="small">{{ notice.title }}</el-tag><span>{{ notice.desc }}</span><el-icon><ArrowRight /></el-icon></div></el-card></div></template>
      <template v-else-if="activeMenu === 'orders'"><div class="welcome-row"><div><h2>工单管理</h2><p>查看需求、调度和服务状态</p></div><el-button type="primary">新建服务需求</el-button></div><el-card shadow="never"><div class="filter-row"><el-radio-group v-model="orderFilter" size="small"><el-radio-button v-for="status in ['全部','待审核','待分配','服务中','待确认','已完成']" :key="status" :label="status" /></el-radio-group><el-input placeholder="搜索工单编号或客户" class="search-input" clearable><template #prefix><el-icon><Search /></el-icon></template></el-input></div><el-table :data="filteredOrders" stripe><el-table-column prop="no" label="工单编号" width="150" /><el-table-column prop="item" label="服务项目" /><el-table-column prop="customer" label="客户" width="100" /><el-table-column prop="provider" label="服务人员" width="110" /><el-table-column prop="region" label="服务区域" /><el-table-column prop="date" label="预约日期" width="120" /><el-table-column label="状态" width="110"><template #default="scope"><el-tag :type="statusType[scope.row.status] || 'info'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column><el-table-column label="操作" width="90"><template #default><el-button link type="primary">详情</el-button></template></el-table-column></el-table></el-card></template>
      <el-empty v-else :description="`${menuItems.find((item) => item.key === activeMenu)?.label}模块正在接入`" />
    </el-main></el-container>
  </el-container>
</template>
