<template>
  <el-input v-model="filterText" placeholder="Filter keyword" />

  <el-tree
    ref="treeRef"
    class="filter-tree"
    :data="tagTreeData"
    :props="defaultProps"
    default-expand-all
    :filter-node-method="filterNode"
  />
</template>

<script lang="ts" setup>
import { API } from "@/common/axios";
import { commonInfo } from "@/common/commonInfo";
import { TagTypeConstant } from "@/common/constant/TagType";
import { HttpResult } from "@/model/HttpResultModel";
import { Tag, TagNode } from "@/model/TagModel";
import { ElTree, ElInput } from "element-plus";
import { reactive, ref, watch } from "vue";

console.log(process.env);

const filterText = ref("");
const treeRef = ref<InstanceType<typeof ElTree>>();

const list: Tag[] = [
  {
    id: 1,
    tagName: "tag1",
    description: "tag1",
    parentId: null,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 2,
    tagName: "tag2",
    description: "tag2",
    parentId: 1,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 3,
    tagName: "tag3",
    description: "tag3",
    parentId: 1,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 4,
    tagName: "tag4",
    description: "tag4",
    parentId: null,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 5,
    tagName: "tag5",
    description: "tag5",
    parentId: 4,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 6,
    tagName: "tag6",
    description: "tag6",
    parentId: 4,
    tagIcon: TagTypeConstant.COMMON,
  },
];

const getTreeData = (list: Tag[]) => {
  const tagNodeList: TagNode[] = list.map((item) => {
    return {
      id: item.id,
      label: item.tagName,
      tag: item,
      children: [],
    };
  });

  const resultTagNodeList: TagNode[] = [];

  const tagTreeMap: Map<number, TagNode> = new Map<number, TagNode>();
  tagNodeList.forEach((item) => {
    tagTreeMap.set(item.tag.id, item);
    if (item.tag.parentId === null) {
      resultTagNodeList.push(item);
    }
  });

  tagNodeList.forEach((item) => {
    if (item.tag.parentId !== null) {
      const parentNode = tagTreeMap.get(item.tag.parentId);
      if (parentNode != undefined) {
        parentNode.children.push(item);
      }
    }
  });
  return resultTagNodeList;
};
let tagTreeData: TagNode[] = reactive(getTreeData(list));
const defaultProps = {
  children: "children",
  label: "label",
};

API.get("/tag").then((res: any) => {
  const httpResult: HttpResult<Tag[]> = res.data;
  const tmepData = getTreeData(httpResult.data);
  tagTreeData.length = 0;
  tagTreeData.push(...tmepData);
  // console.log(tagTreeData);
  // tagTreeData = reactive(tmepData);
});

watch(filterText, (val) => {
  treeRef.value?.filter(val);
});

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const filterNode: any = (value: string, data: TagNode): boolean => {
  if (!value) return true;
  return data.label.includes(value);
};

// tagTreeData = reactive(getTreeData(list));
</script>

<style lang="less" scoped></style>
