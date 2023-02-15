<template>
  <div class="tag-container">
    <el-input v-model="filterText" placeholder="Filter keyword" />

    <el-tree
      ref="treeRef"
      class="filter-tree"
      :data="tagTreeData"
      :props="defaultProps"
      default-expand-all
      :filter-node-method="filterNode"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <div>
            <el-dropdown>
              <span class="el-dropdown-link">
                {{ node.label }}
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>Add geng</el-dropdown-item>
                  <el-dropdown-item>edit</el-dropdown-item>
                  <el-dropdown-item>delete</el-dropdown-item>
                  <el-dropdown-item>merge</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <span>
            <el-button type="primary" @click.stop="append(data)" link
              >Add</el-button
            >
          </span>
        </span>
      </template></el-tree
    >
  </div>
</template>

<script lang="ts" setup>
import { mockTagList } from "@/assets/data/tagListData";
import { API } from "@/common/axios";
import { commonInfo } from "@/common/commonInfo";
import { TagTypeConstant } from "@/common/constant/TagType";
import { HttpResult } from "@/model/HttpResultModel";
import { Tag, TagNode } from "@/model/TagModel";
import { ElTree, ElInput } from "element-plus";
import { reactive, ref, watch } from "vue";

const filterText = ref("");
const treeRef = ref<InstanceType<typeof ElTree>>();

const myMockTagList: Tag[] = mockTagList;

const getTreeData = (list: Tag[]) => {
  const tagNodeList: TagNode[] = list.map((item) => {
    return new TagNode(item);
  });

  const resultTagNodeList: TagNode[] = [];

  const tagTreeMap: Map<number, TagNode> = new Map<number, TagNode>();
  tagNodeList.forEach((item) => {
    tagTreeMap.set(item.id, item);
    if (item.parentId === null) {
      resultTagNodeList.push(item);
    }
  });

  tagNodeList.forEach((item) => {
    if (item.parentId !== null) {
      const parentNode = tagTreeMap.get(item.parentId);
      if (parentNode != undefined) {
        parentNode.children.push(item);
      }
    }
  });
  return resultTagNodeList;
};
let tagTreeData: TagNode[] = reactive([]);
const defaultProps = {
  children: "children",
  label: "tagName",
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
  return data.tagName.includes(value);
};

const append = (data: TagNode) => {
  commonInfo.addSearchTag(data);
};

// tagTreeData = reactive(getTreeData(myMockTagList));
</script>

<style lang="less" scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.el-dropdown-link {
  cursor: pointer;
}

.tag-container {
  height: 100%;
  padding: 10px;
}
</style>
