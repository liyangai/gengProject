<template>
  <div>iscollapse {{ globaInfo.isCollapse }}</div>
</template>

<script lang="ts" setup>
import { commonInfo } from "@/common/commonInfo";
import { reactive } from "vue";

const globaInfo = reactive(commonInfo);
</script>

<style lang="less" scoped></style>
