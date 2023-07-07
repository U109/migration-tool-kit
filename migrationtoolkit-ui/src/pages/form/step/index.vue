<template>
  <t-card :bordered="false">
    <t-dialog width="50%" maxHeight="100px" :visible.sync="visible">
      <t-table
        rowKey="index"
        :data="data"
        :footData="[{}]"
        :columns="columns"
        :table-layout="tableLayout"
        :table-content-width="tableLayout === 'fixed' ? undefined : '1600px'"
        :max-height="500"
        :fixedRows="fixedTopAndBottomRows ? [2, 2] : undefined"
        :scroll="virtualScroll ? { type: 'virtual' } : undefined"
        :stripe="stripe"
        :selected-row-keys="selectedRowKeys"
        @select-change="rehandleSelectChange"
        bordered
        size="small"
      >
      </t-table>
    </t-dialog>
    <div class="form-step-container">
      <!-- 简单步骤条 -->
      <t-card :bordered="false">
        <t-steps :defaultCurrent="1" :current="activeForm" status="process">
          <t-step-item title="任务摘要" content="基本信息"></t-step-item>
          <t-step-item title="对象选取" content="迁移对象选取"></t-step-item>
          <t-step-item title="类型映射" content="数据类型映射设置"></t-step-item>
          <t-step-item title="完成" content=""></t-step-item>
        </t-steps>
      </t-card>

      <!-- 分步表单1 :rules="rules" -->
      <t-form
        v-show="activeForm === 0"
        class="step-form"
        :data="formData2"
        labelAlign="right"
        @submit="onSubmit1"
        colon
      >
        <t-form-item label="任务名称" name="title">
          <t-input :style="{ width: '480px' }" v-model="formData2.title" placeholder="请输入任务名称"></t-input>
        </t-form-item>
        <t-form-item label="任务描述" name="description">
          <t-textarea :style="{ width: '480px' }" v-model="formData2.description"
                      :maxcharacter="200" placeholder="请输入任务描述"></t-textarea>
        </t-form-item>
        <t-form-item label="源数" name="source">
          <t-select :style="{ width: '390px' }" v-model="formData2.source" class="demo-select-base" clearable>
            <t-option v-for="(item, index) in sourceOptions" :value="item.value" :label="item.name" :key="index">
              {{ item.name }}
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="目标库" name="target">
          <t-select :style="{ width: '390px' }" v-model="formData2.target" class="demo-select-base" clearable>
            <t-option v-for="(item, index) in targetOptions" :value="item.value" :label="item.name" :key="index">
              {{ item.name }}
            </t-option>
          </t-select>
        </t-form-item>
        <t-form-item>
          <t-button theme="primary" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>

      <!-- 分步表单2 -->
      <t-form
        v-show="activeForm === 1"
        class="step-form"
        :data="formData1"
        :rules="rules"
        labelAlign="right"
        @reset="onReset2"
        @submit="onSubmit2"
        colon
      >
        <t-form-item class="amount-label" label="迁移表">

          <t-tag theme="primary" variant="light">21 / 50</t-tag>
          <t-button @click="visible = true" style="margin-left: 50px">配置迁移表</t-button>

        </t-form-item>

        <t-form-item label="迁移表设置" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移表结构</t-checkbox>
            <t-checkbox value="2">迁移数据</t-checkbox>
            <t-checkbox value="3">重建表</t-checkbox>
          </t-checkbox-group>
        </t-form-item>
        <t-form-item label="约束设置" name="name">
          <t-checkbox-group v-model="value2">
            <t-checkbox :checkAll="true" label="全选"/>
            <t-checkbox label="主键" value="1"/>
            <t-checkbox label="外键" value="2"/>
            <t-checkbox label="索引" value="3"/>
            <t-checkbox label="非空" value="4"/>
            <t-checkbox label="唯一" value="5"/>
            <t-checkbox label="检查" value="6"/>
            <t-checkbox label="默认值" value="7"/>
          </t-checkbox-group>
        </t-form-item>
        <t-form-item label="序列对象" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移数量：</t-checkbox>
          </t-checkbox-group>
          <t-tag theme="primary" variant="light">21</t-tag>
        </t-form-item>
        <t-form-item label="视图对象" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移数量：</t-checkbox>
          </t-checkbox-group>
          <t-tag theme="primary" variant="light">21</t-tag>
        </t-form-item>
        <t-form-item label="存储过程对象" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移数量：</t-checkbox>
          </t-checkbox-group>
          <t-tag theme="primary" variant="light">21</t-tag>
        </t-form-item>
        <t-form-item label="同义词对象" name="type">
          <t-checkbox-group>
            <t-checkbox value="1">迁移数量：</t-checkbox>
          </t-checkbox-group>
          <t-tag theme="primary" variant="light">21</t-tag>
        </t-form-item>
        <t-form-item>
          <t-button type="reset" theme="default" variant="base">上一步</t-button>
          <t-button theme="primary" style="margin-left: 20px" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>


      <!-- 分步表单3 -->
      <t-form
        v-show="activeForm === 2"
        class="step-form"
        labelAlign="left"
        @reset="onReset3"
        @submit="onSubmit3"
      >
        <t-form-item>
          <t-table
            ref="tableRef"
            row-key="key"
            :columns="typecolumns"
            :data="typedata"
            bordered
            @row-validate="onRowValidate"
          />
        </t-form-item>
        <t-form-item>
          <t-button type="reset" theme="default" variant="base">上一步</t-button>
          <t-button theme="primary" style="margin-left: 20px" type="submit">下一步</t-button>
        </t-form-item>
      </t-form>

      <!-- 分步表单4 -->
      <div class="step-form-4" v-show="activeForm === 6">
        <check-circle-filled-icon style="color: green" size="52px"/>
        <p class="text">新建任务完成</p>
        <p class="tips">请在迁移期间保持连接畅通</p>
        <div class="button-group">
          <t-button @click="onReset4" theme="primary">新建任务</t-button>
          <t-button @click="onSubmit4" variant="base" style="margin-left: 20px" theme="default">查看详情</t-button>
        </div>
      </div>
    </div>
  </t-card>
</template>
<script lang="jsx">
import {CheckCircleFilledIcon} from 'tdesign-icons-vue';
import {prefix} from '@/config/global';
import {
  Input, Select, MessagePlugin,
} from 'tdesign-vue';

const initData = new Array(5).fill(null).map((_, i) => ({
  key: String(i + 1),
  sourceDataType: ['int', 'integer', 'float'][i % 3],
  targetDataType: i % 3,
  dataLength: [0, 0, 0, 0, 0,][i % 4],
  dataPrecision: [0, 0, 0, 0][i % 4],
}));
const INITIAL_DATA2 = {
  title: '',
  description: '',
  source: '',
  target: ''
};
const STATUS_OPTIONS = [
  {label: 'int', value: 0},
  {label: 'integer', value: 1},
  {label: 'float', value: 2},
];
const data = new Array(100).fill(null).map((_, i) => ({
  index: i + 1,
  sourceTableName: ['AA', 'BB', 'CC'][i % 3],
  targetTableName: ['AA', 'BB', 'CC'][i % 3],
  tableType: '普通表',
  dataCount: '10000',
}));
export default {
  name: 'FormStep',
  components: {
    CheckCircleFilledIcon,
  },
  data() {
    return {
      prefix,
      formData1: {...INITIAL_DATA2},
      formData2: {...INITIAL_DATA2},
      sourceOptions: [
        {name: 'MySql', value: '1'},
        {name: 'Oracle', value: '2'},
        {name: 'SqlServer', value: '3'},
      ],
      targetOptions: [
        {name: 'MySql', value: '1'},
        {name: 'Oracle', value: '2'},
        {name: 'SqlServer', value: '3'},
      ],
      value2: ['选项一'],
      rules: {
        source: [{required: true, message: '请选择源数据库类型', type: 'error'}],
        target: [{required: true, message: '请选择目标数据库类型', type: 'error'}],
        title: [{required: true, message: '请输入任务名称', type: 'error'}],
      },
      activeForm: 0,
      visible: false,
      virtualScroll: false,
      fixedTopAndBottomRows: false,
      stripe: false,
      tableLayout: 'fixed',
      data,
      columns: [
        {
          colKey: 'row-select',
          type: 'multiple',
          width: 50,
        },
        {
          colKey: 'sourceTableName',
          title: '源表名',
          width: '100',
          foot: '共20条',
        },
        {
          colKey: 'targetTableName',
          title: '目标表名',
          width: '100',
          fixed: 'left',
        },
        {
          colKey: 'tableType',
          title: '类型',
          width: '120',
          foot: '-',
        },
        {
          colKey: 'dataCount',
          title: '表记录数',
          width: '150',
          foot: '-',
        }
      ],
      selectedRowKeys: [],
      typedata: [...initData],
      align: 'left'

    };
  },
  computed: {
    typecolumns() {
      return [
        {
          title: '源数据类型',
          colKey: 'sourceDataType',
          align: this.align
        },
        {
          title: '目标数据库类型',
          colKey: 'targetDataType',
          cell: (h, {row}) => STATUS_OPTIONS.find((t) => t.value === row.targetDataType)?.label,
          edit: {
            component: Select,
            // props, 透传全部属性到 Select 组件
            props: {
              clearable: true,
              options: STATUS_OPTIONS,
            },
            // 除了点击非自身元素退出编辑态之外，还有哪些事件退出编辑态
            // abortEditOnEvent: ['onChange'],
            // 编辑完成，退出编辑态后触发
            onEdited: (context) => {
              this.typedata.splice(context.rowIndex, 1, context.newRowData);
              console.log('Edit Framework:', context);
              MessagePlugin.success('Success');
            },
          },
        },
        {
          title: '长度',
          colKey: 'dataLength',
          edit: {
            component: Input,
            // props, 透传全部属性到 Input 组件（可以是一个函数，不同行有不同的 props 属性 时，使用 Function）
            props: {
              clearable: true,
              autofocus: true,
            },
            // 除了点击非自身元素退出编辑态之外，还有哪些事件退出编辑态
            abortEditOnEvent: ['onEnter'],
            // 编辑完成，退出编辑态后触发
            onEdited: (context) => {
              context.newRowData.dataLength = Number(context.newRowData.dataLength);
              this.typedata.splice(context.rowIndex, 1, context.newRowData);
              console.log('Edit firstName:', context);
              MessagePlugin.success('Success');
            },
            // 默认是否为编辑状态
            defaultEditable: true,
            // 校验时机：exit | change
            validateTrigger: 'change',
            // 透传给 component: Input 的事件
            on: (editContext) => ({
              onBlur: () => {
                console.log('失去焦点', editContext);
              },
              // both onEnter and enter can work
              onEnter: (ctx) => {
                console.log('回车', ctx);
              },
            }),
          },
        },
        {
          title: '精度',
          colKey: 'dataPrecision',
          edit: {
            component: Input,
            props: {
              clearable: true,
              autofocus: true,
            },
            abortEditOnEvent: ['onEnter'],
            onEdited: (context) => {
              context.newRowData.dataPrecision = Number(context.newRowData.dataPrecision);
              this.typedata.splice(context.rowIndex, 1, context.newRowData);
              console.log('Edit firstName:', context);
              MessagePlugin.success('Success');
            },
            defaultEditable: true,
            validateTrigger: 'change',
            on: (editContext) => ({
              onBlur: () => {
                console.log('失去焦点', editContext);
              },
              onEnter: (ctx) => {
                console.log('回车', ctx);
              },
            }),
          },
        },
      ];
    },
  },
  methods: {
    onRowValidate(params) {
      console.log('validate:', params);
    },
    // 用于提交前校验数据（示例代码有效，勿删）
    validateTableData() {
      // 仅校验处于编辑态的单元格
      this.$refs.tableRef.validateTableData().then((result) => {
        console.log('validate result: ', result);
      });
    },
    rehandleClickOp(context) {
      console.log(context);
    },
    rehandleSelectChange(value, {selectedRowData}) {
      this.selectedRowKeys = value;
      console.log(value, selectedRowData);
    },
    onSubmit1({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 1;
      }
    },
    onSubmit2({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 2;
      }
    },
    onReset2() {
      this.activeForm = 0;
    },
    onSubmit3({validateResult}) {
      if (validateResult === true) {
        this.activeForm = 6;
      }
    },
    onReset3() {
      this.activeForm = 1;
    },
    onSubmit4() {
      this.$router.replace({path: '/detail/base'});
    },
    onReset4() {
      this.activeForm = 0;
    },
  },
};
</script>
<style lang="less" scoped>
@import './index';

.link {
  cursor: pointer;
}
</style>
