<template>
  <div class="geng-container">
    <div class="operation-container">
      <div class="search-container">
        <el-input v-model="searchModel" placeholder="Please input" />
      </div>
      <div class="button-container">
        <el-button type="primary" @click="addGeng">添加</el-button>
        <el-button type="primary" @click="refreshGengList">刷新</el-button>
        <el-button type="primary">清空</el-button>
      </div>
      <div class="tag-container">
        <el-tag
          v-for="tag in selectSearchTags"
          :key="tag.id"
          class="tag-item"
          @close="deleteTag(tag)"
          closable
        >
          {{ tag.tagName }}
        </el-tag>
      </div>
    </div>
    <div class="table-container">
      <el-table
        ref="multipleTableRef"
        :data="gengTableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          show-overflow-tooltip
          property="resume"
          label="resume"
          width="120"
        />
        <el-table-column
          show-overflow-tooltip
          property="tagNames"
          label="tagNames"
          width="120"
        />
        <el-table-column label="description">
          <template #default="scope">
            <el-tooltip placement="top" :show-after="500">
              <template #content>
                <div class="description-tip">
                  <span class="pre-wrap-important">
                    {{ scope.row.description }}
                  </span>
                </div>
              </template>
              <span class="description-cell cell el-tooltip">{{
                scope.row.description
              }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="Opreations" width="240">
          <template #default="scope"
            ><el-button
              size="small"
              @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >
            <el-button
              size="small"
              @click="handleDetail(scope.$index, scope.row)"
              >详情</el-button
            >
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            ></template
          >
        </el-table-column>
      </el-table>
    </div>
  </div>
  <GengItem
    v-if="commonInfo.gengItemDialogOpen.value"
    :selectGeng="selectGeng"
    @close="onClose"
  ></GengItem>
</template>

<script lang="ts" setup>
import { mockGengList } from "@/assets/data/GengListData";
import { mockTagList } from "@/assets/data/tagListData";
import { API } from "@/common/axios";
import { commonInfo, DialogType } from "@/common/commonInfo";
import { Geng, GengNode } from "@/model/GengModel";
import { HttpResult } from "@/model/HttpResultModel";
import { Tag, TagNode } from "@/model/TagModel";
import { ElMessageBox, ElMessage } from "element-plus";
import { reactive, Ref, ref, watch } from "vue";
import GengItem from "./GengItem.vue";

const tagList: TagNode[] = commonInfo.tagNodeList;
const gengList: Geng[] = reactive([]);

const gengTableData: GengNode[] = reactive([]);
const multipleSelection = ref<GengNode[]>([]);

const searchModel = "";
let selectGeng = ref(GengNode.getEmptyObj());

const selectSearchTags: TagNode[] = commonInfo.searchTags;

watch(gengList, () => {
  getGengTableData();
});
watch(tagList, () => {
  getGengTableData();
});

//数据处理函数
const getGengTableData = () => {
  const tagMap = new Map<number, string>();
  const result: GengNode[] = [];
  tagList.forEach((tag) => {
    tagMap.set(tag.id, tag.tagName);
  });
  gengList.forEach((geng) => {
    const gengNode: GengNode = new GengNode(geng);
    gengNode.tagNames = gengNode.tagIds.map((id) => {
      return tagMap.get(id) || "";
    });
    result.push(gengNode);
  });
  gengTableData.length = 0;
  gengTableData.push(...result);
};

//html 绑定函数
const handleSelectionChange = (val: GengNode[]) => {
  multipleSelection.value = val;
};

const handleDetail = (index: number, row: GengNode) => {
  console.log(index, row);
};

const addGeng = () => {
  commonInfo.currentDialogType.value = DialogType.Add;
  selectGeng.value = GengNode.getEmptyObj();
  commonInfo.gengItemDialogOpen.value = true;
};

const handleEdit = (index: number, row: GengNode) => {
  commonInfo.currentDialogType.value = DialogType.Modify;
  selectGeng.value = row;
  commonInfo.gengItemDialogOpen.value = true;
  console.log(index, row);
};
const handleDelete = (index: number, row: GengNode) => {
  ElMessageBox.confirm("确认删除geng: " + row.resume + " ?", "Warning", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    API.delete("/geng/" + row.id).then((res) => {
      if (res === null) {
        return;
      }
      refreshGengList();
      ElMessage({
        type: "success",
        message: "删除成功",
      });
    });
  });

  console.log(index, row);
};

const deleteTag = (tag: TagNode) => {
  commonInfo.removeTag(tag);
};

const onClose = (geng: Geng) => {
  commonInfo.refreshTagNodeList();
  refreshGengList();
};

const refreshGengList = () => {
  const searchIds = selectSearchTags.map((item) => item.id);
  API.post("/geng/getByTagIds", searchIds)
    .then((res: any) => {
      const httpResult: HttpResult<Geng[]> = res;
      gengList.length = 0;
      gengList.push(...httpResult.data);
    })
    .catch(() => {
      gengList.length = 0;
    });
};

refreshGengList();
</script>

<style lang="less">
.pre-wrap-important {
  white-space: pre-wrap !important;
}
.description-cell {
  display: inline-block;
  width: 100%;
}

.description-tip {
  background-color: #fff;
  color: #000;
  padding: 5px;
  font-size: 14px;
}

.operation-container {
  display: flex;
  margin-bottom: 20px;
}
.search-container {
  width: 150px;
  margin-right: 20px;
}

.button-container {
  width: 220px;
}

.geng-container {
  height: 100%;
  padding: 10px;
}

.tag-container {
  flex: 1;
}

.tag-item {
  margin-right: 15px;
  cursor: pointer;
}
</style>
