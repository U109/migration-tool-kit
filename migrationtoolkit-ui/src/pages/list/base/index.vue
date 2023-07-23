<template>
  <div>
    <t-card class="list-card-container" :bordered="false">
      <t-row justify="space-between">
        <div class="left-operation-container">
          <t-button @click="handleSetupContract"> 新建数据源 </t-button>
        </div>
        <t-input v-model="searchValue" class="search-input" placeholder="请输入你需要搜索的内容" clearable>
          <template #suffix-icon>
            <search-icon size="20px" />
          </template>
        </t-input>
      </t-row>

      <div class="table-container">
        <t-table :columns="columns" :data="data" :rowKey="rowKey" :verticalAlign="verticalAlign" :hover="hover"
          :pagination="pagination" :loading="dataLoading"
          @page-change="rehandlePageChange" @change="rehandleChange" @select-change="rehandleSelectChange"
          :headerAffixedTop="true" :headerAffixProps="{ offsetTop: offsetTop, container: getContainer }">
          <template #op="slotProps">
            <a class="t-button-link" @click="handleClickDetail()">详情</a>
            <a class="t-button-link" @click="handleClickDelete(slotProps)">删除</a>
          </template>
        </t-table>
      </div>
    </t-card>
    <t-dialog header="确认删除当前所选数据源？" :body="confirmBody" :visible.sync="confirmVisible" @confirm="onConfirmDelete"
      :onCancel="onCancel">
    </t-dialog>
  </div>
</template>
<script lang="ts">
import Vue from 'vue';
import { SearchIcon } from 'tdesign-icons-vue';
import { prefix } from '@/config/global';


export default Vue.extend({
  name: 'ListBase',
  components: {
    SearchIcon
  },
  data() {
    return {
      prefix,
      dataLoading: false,
      data: [],
      value: 'first',
      columns: [
        {
          title: '连接名称',
          width: 200,
          ellipsis: true,
          colKey: 'connName',
        },
        { title: '数据源类型', colKey: 'dbType', width: 200 },
        {
          title: '服务器地址',
          width: 200,
          ellipsis: true,
          colKey: 'host',
        },
        {
          title: '端口',
          width: 150,
          ellipsis: true,
          colKey: 'port',
        },
        {
          title: '数据库/Schema',
          width: 200,
          ellipsis: true,
          colKey: 'dbName',
        },
        {
          title: '用户名',
          width: 200,
          ellipsis: true,
          colKey: 'username',
        },
        {
          align: 'left',
          fixed: 'right',
          width: 200,
          colKey: 'op',
          title: '操作',
        },
      ],
      rowKey: 'index',
      tableLayout: 'auto',
      verticalAlign: 'top',
      hover: true,
      rowClassName: (rowKey: string) => `${rowKey}-class`,
      // 与pagination对齐
      pagination: {
        defaultPageSize: 10,
        total: 0,
        defaultCurrent: 1,
        current:1
      },
      current: 1,
      searchValue: '',
      confirmVisible: false,
      deleteIdx: -1,
    };
  },
  computed: {
    confirmBody() {
      if (this.deleteIdx > -1) {
        const { connName } = this.data?.[this.deleteIdx];
        return `删除后，${connName}的数据源信息将被删除，且无法恢复`;
      }
      return '';
    },
    offsetTop() {
      return this.$store.state.setting.isUseTabsRouter ? 48 : 0;
    },
  },
  mounted() {

  },
  created() {
    this.getDataBaseConnInfoList();
  },
  activated() {
    this.getDataBaseConnInfoList();
  },
  methods: {
    getDataBaseConnInfoList() {
      this.dataLoading = true;
      this.$request
        .get("/data-base/getDataBaseConnInfoList")
        .then((res) => {
          if (res.data.code === 200) {
            this.data = res.data.result;
            this.pagination = {
              ...this.pagination,
              total: res.data.result.length,
              current: this.current
            };
            console.log("this.current : ",this.current)
          }
        })
        .catch((e) => {
          console.log(e);
        })
        .finally(() => { this.dataLoading = false; });
    },
    getContainer() {
      return document.querySelector('.tdesign-starter-layout');
    },
    rehandlePageChange(curr, pageInfo) {
      this.pagination.current = curr.current
      this.current = curr.current;
      console.log('分页变化', curr, pageInfo);
    },
    rehandleSelectChange(selectedRowKeys: number[]) {
      this.selectedRowKeys = selectedRowKeys;
    },
    rehandleChange(changeParams, triggerAndData) {
      console.log('统一Change', changeParams, triggerAndData);
    },
    handleClickDetail() {
      this.$router.push('/detail/base');
    },
    handleSetupContract() {
      this.$router.push('/form/base');
    },
    handleClickDelete(row: { rowIndex: any }) {
      this.deleteIdx = row.rowIndex;
      this.confirmVisible = true;
    },
    onConfirmDelete() {
      // 真实业务请发起请求
      this.data.splice(this.deleteIdx, 1);
      this.pagination.total = this.data.length;
      const selectedIdx = this.selectedRowKeys.indexOf(this.deleteIdx);
      if (selectedIdx > -1) {
        this.selectedRowKeys.splice(selectedIdx, 1);
      }
      this.confirmVisible = false;
      this.$message.success('删除成功');
      this.resetIdx();
    },
    onCancel() {
      this.resetIdx();
    },
    resetIdx() {
      this.deleteIdx = -1;
    },
  },
});
</script>

<style lang="less" scoped>
@import '@/style/variables';

.payment-col {
  display: flex;

  .trend-container {
    display: flex;
    align-items: center;
    margin-left: 8px;
  }
}

.left-operation-container {
  padding: 0 0 6px 0;
  margin-bottom: 16px;

  .selected-count {
    display: inline-block;
    margin-left: var(--td-comp-margin-s);
    color: var(--td-text-color-secondary);
  }
}

.search-input {
  width: 360px;
}

.t-button+.t-button {
  margin-left: var(--td-comp-margin-s);
}
</style>
