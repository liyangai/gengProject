<template>
  <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false">
    <el-menu-item index="0">LOGO</el-menu-item>
    <div class="flex-grow" />
  </el-menu>
  <!-- <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
    <el-radio-button :label="false">expand</el-radio-button>
    <el-radio-button :label="true">collapse</el-radio-button>
  </el-radio-group> -->
  <div class="page-container">
    <div class="sider-bar" :class="{ collapse: globaInfo.isCollapse }">
      <el-menu
        :collapse-transition="false"
        default-active="1"
        class="el-menu-vertical-demo"
        :collapse="globaInfo.isCollapse"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-menu-item index="1" @click="handleToggle">
          <el-icon><document /></el-icon>
        </el-menu-item>
        <el-menu-item index="2">
          <el-icon><document /></el-icon>
          <template #title>geng list</template>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="page-content">
      <el-row>
        <el-col :span="6"><TagList></TagList></el-col>
        <el-col :span="18"><GengList></GengList></el-col>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import GengList from "./views/GengList.vue";
import TagList from "./views/TagList.vue";
import { commonInfo } from "./common/commonInfo";
const globaInfo = reactive(commonInfo);

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};

const handleToggle = () => {
  globaInfo.setIsCollapse(!globaInfo.isCollapse);
};

commonInfo.refreshTagNodeList();
</script>

<style scoped>
.page-container {
  height: calc(100% - 60px);
  width: 100%;
  display: flex;
}

.sider-bar {
  width: 155px;   
}

.sider-bar.collapse {
  width: 64px;
}

.page-content {
  flex: 1;
  height: 100%;
}
</style>
