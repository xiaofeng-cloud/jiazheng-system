<script setup>
import { ref } from 'vue'

const systemStatus = ref('开发基线已就绪')
</script>

<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div>
        <div class="app-title">家政服务工单管理系统</div>
        <div class="app-subtitle">工单调度与服务质量追踪</div>
      </div>
      <el-tag type="success">{{ systemStatus }}</el-tag>
    </el-header>

    <el-main class="app-main">
      <el-card shadow="never">
        <template #header>
          <span>项目开发首页</span>
        </template>
        <el-empty description="业务模块将在后续阶段逐步接入" />
      </el-card>
    </el-main>
  </el-container>
</template>
