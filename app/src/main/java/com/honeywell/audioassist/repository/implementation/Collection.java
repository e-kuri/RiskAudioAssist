package com.honeywell.audioassist.repository.implementation;

public enum Collection {

        Event("Events");

        private final String name;

        Collection(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

}
