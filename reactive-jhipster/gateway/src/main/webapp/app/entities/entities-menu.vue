<template>
  <div>
    <b-dropdown-item to="/blog">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.blogBlog')">Blog</span>
    </b-dropdown-item>
    <b-dropdown-item to="/post">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.blogPost')">Post</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tag">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.blogTag')">Tag</span>
    </b-dropdown-item>
    <b-dropdown-item to="/product">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.storeProduct')">Product</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
