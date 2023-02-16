<template>
  <el-dialog v-model="dialogFormVisible" title="Shipping address">
    <el-form :model="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="resume" :label-width="formLabelWidth">
            <el-input v-model="form.resume" autocomplete="off" />
          </el-form-item>
          <el-form-item label="tagNames" :label-width="formLabelWidth">
            <el-autocomplete
              v-model="tagSearch"
              :fetch-suggestions="querySearch"
              clearable
              value-key="tagName"
              class="inline-input w-50"
              placeholder="Please Input"
              :select-when-unmatched="true"
              @select="handleSelect"
              @change="handleChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12"
          ><el-form-item label="desc" :label-width="formLabelWidth">
            <el-input
              v-model="form.description"
              autocomplete="off"
              :rows="5"
              type="textarea"
            /> </el-form-item
        ></el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirm"> Confirm </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { API } from "@/common/axios";
import { commonInfo } from "@/common/commonInfo";
import { TagTypeConstant } from "@/common/constant/TagType";
import { Geng, GengNode } from "@/model/GengModel";
import { HttpResult } from "@/model/HttpResultModel";
import { Tag, TagNode } from "@/model/TagModel";
import { ElTree, ElInput } from "element-plus";
import { reactive, ref, watch, defineProps, withDefaults, Ref } from "vue";

let dialogFormVisible = reactive(commonInfo.gengItemDialogOpen);

let tagSearch = ref("");

interface Props {
  selectGeng: GengNode;
  tagList: Tag[];
}

const formLabelWidth = "100px";
// const props = defineProps<Props>();
const props = withDefaults(defineProps<Props>(), {
  selectGeng: () => GengNode.getEmptyObj(),
  labels: () => [],
});
const form = reactive(Object.assign({}, props.selectGeng));

const confirm = () => {
  dialogFormVisible.value = false;
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const querySearch = (queryString: string, cb: any) => {
  const results = queryString
    ? props.tagList.filter((item) =>
        item.tagName.toLowerCase().includes(queryString.toLowerCase())
      )
    : props.tagList;
  // call callback function to return suggestions
  cb(results);
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const handleSelect = (item: any) => {
  console.log(item);
};

const handleChange = (item: string) => {
  console.log(item);
};
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
